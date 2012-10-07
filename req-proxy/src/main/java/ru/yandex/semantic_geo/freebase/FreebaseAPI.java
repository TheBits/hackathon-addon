package ru.yandex.semantic_geo.freebase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: rasifiel
 * Date: 06.10.12
 * Time: 14:42
 */
public class FreebaseAPI {

    private final static String API_KEY = "AIzaSyCrVFOFBEyP9EzZIcKcgaYbuMLFZqjw5XE";

    public final String version;

    public FreebaseAPI(final String version) {
        this.version = version;
    }

    private final static String URL_BASE = "https://www.googleapis.com/freebase/";

    public JSONObject request(final String urlSuffix, final FreebaseQuery query) {
        URI uri = null;
        try {
            uri = new URI("https", "www.googleapis.com", "/freebase/" + version + "/" + urlSuffix,
                    query.toQuery() + "&key=" + API_KEY, null);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Exception while url parsing query");
        }
        final String url = String.format("%s%s/%s?%s&key=%s", URL_BASE, version, urlSuffix, query.toQuery(), API_KEY);
        URL connection;
        try {
            connection = uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Exception while url parsing query " + url);
        }
        try {
            InputStream ioS = connection.openStream();
            return new JSONObject(new JSONTokener(ioS));
        } catch (IOException e) {
            throw new RuntimeException("IOException while execution of query " + url);
        } catch (JSONException e) {
            throw new RuntimeException("JSONException while execution of query " + url);
        }
    }

    public JSONObject topicRequest(final String topicId, final FreebaseQuery query) {
        return request("topic" + topicId, query);
    }

    public JSONObject textRequest(final String topicId, final FreebaseQuery query) {
        return request("text" + topicId, query);
    }

    public JSONObject mqlRequest(final FreebaseQuery query) {
        return request("mqlread", query);
    }


    private static final String MQL_REQ =
            "{ \"mid\": \"%s\", \"/common/topic/image\": [{ \"mid\": null, \"optional\": true }], \"/type/object/name\": null, \"/location/location/geolocation\": { \"latitude\": null, \"longitude\": null, \"optional\": true }, \"/location/statistical_region/population\": [{ \"number\": null, \"year\": null, \"optional\": true }], \"/location/location/area\": { \"value\": null, \"optional\": true }, \"/location/location/containedby\": [{ \"name\": null, \"mid\": null, \"optional\": true }], \"/location/location/contains\": [{ \"name\": null, \"mid\": null, \"optional\": true }] }";

    public Map<String, FreebaseObject> getInfo(final Set<String> mids) {
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(100, 1000, 10000, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        final Map<String, FreebaseObject> result = new HashMap<String, FreebaseObject>();
        final Map<String, Future<JSONObject>> textDesc = new HashMap<String, Future<JSONObject>>();
        final Map<String, Future<JSONObject>> imgAttrs = new HashMap<String, Future<JSONObject>>();
        for (final String mid : mids) {
            textDesc.put(mid, executor.submit(new Callable<JSONObject>() {
                public JSONObject call() throws Exception {
                    return textRequest(mid, new FreebaseQuery().add("maxlength", "200").add("format", "plain"));
                }
            }));
            imgAttrs.put(mid, executor.submit(new Callable<JSONObject>() {
                public JSONObject call() throws Exception {
                    return mqlRequest(new FreebaseQuery().add("query", MQL_REQ.replace("%s", mid)));
                }
            }));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
        }
        for (final String mid : mids) {
            try {
                final String text = textDesc.get(mid).get().get("result").toString();
                final JSONObject imgAttr = (JSONObject) imgAttrs.get(mid).get().get("result");
                final String imgMid = getImgMid(imgAttr);
                final JSONObject attrs = getAttrs(imgAttr);
                result.put(mid, new FreebaseObject(text, mid, imgMid, attrs));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private class Pair<X extends Comparable, Y> implements Comparable<Pair<X, Y>> {
        public X a;
        public Y b;

        public Pair(final X year, final Y number) {
            a = year;
            b = number;
        }

        public int compareTo(final Pair<X, Y> o) {
            return -a.compareTo(o.a);
        }
    }

    private String getImgMid(final JSONObject obj) throws JSONException {
        JSONArray imgs = (JSONArray) obj.get("/common/topic/image");
        if (imgs.length() == 0) {
            return null;
        }
        return ((JSONObject) imgs.get(0)).get("mid").toString();
    }

    private static final String[] COPY_KEYS =
            {"/location/location/area", "/location/location/containedby", "/location/location/contains",
                    "/location/location/geolocation", "/type/object/name"};

    private JSONObject getAttrs(final JSONObject obj) throws JSONException {
        final JSONObject result = new JSONObject();
        for (final String key : COPY_KEYS) {
            if (obj.has(key)) {
                String[] prts = key.split("/");
                Object o = obj.get(key);
                if (o instanceof JSONArray) {
                    JSONArray ar = (JSONArray) o;
                    JSONArray newAr = new JSONArray();
                    for (int i=0;i<Math.min(ar.length(),3);i++)
                        newAr.put(ar.get(i));
                    result.put(prts[prts.length - 1],newAr);
                } else {
                    result.put(prts[prts.length - 1],o);
                }
            }
        }
        if (obj.has("/location/statistical_region/population")) {
            final JSONArray populations = (JSONArray) obj.get("/location/statistical_region/population");
            final List<Pair<Long, Long>> pops = new LinkedList<Pair<Long, Long>>();
            for (int i = 0; i < populations.length(); i++) {
                final JSONObject pair = (JSONObject) populations.get(i);
                try {
                    Pair<Long, Long> pr = new Pair<Long, Long>(Long.valueOf(pair.get("year").toString()),
                            Long.valueOf(pair.get("number").toString()));
                    pops.add(pr);
                } catch (Exception ee) {
                }
            }
            Collections.sort(pops);
            if (pops.size() > 0) {
                result.put("population", pops.get(0).b);
            }
        }
        return result;
    }

    public static void mid2url(final JSONObject object) {
        Iterator it = object.keys();
        while (it.hasNext()) {
            Object o = it.next();
            try {
                if (object.get(o.toString()) instanceof JSONObject) {
                    mid2url((JSONObject) object.get(o.toString()));
                } else if (object.get(o.toString()) instanceof JSONArray) {
                    JSONArray ar = (JSONArray) object.get(o.toString());
                    mid2url(ar);
                }
            } catch (JSONException e) {
            }
        }
        if (object.has("mid")) {
            try {
                object.put("url", "http://freebase.com/view" + object.get("mid").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void mid2url(final JSONArray ar) {
        for (int i = 0; i < ar.length(); i++) {
            try {
                if (ar.get(i) instanceof JSONObject) {
                    mid2url((JSONObject) ar.get(i));
                }
                if (ar.get(i) instanceof JSONArray) {
                    mid2url((JSONArray) ar.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
