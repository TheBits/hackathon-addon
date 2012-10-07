package ru.yandex.semantic_geo.freebase;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: rasifiel
 * Date: 06.10.12
 * Time: 18:43
 */
public class FreebaseAPITest extends junit.framework.TestCase {
    public void testGetInfo() throws Exception {
        FreebaseAPI api = new FreebaseAPI("v1");
        Set<String> mids = new HashSet<String>();
        mids.add("/m/0233n3");
        Map<String, FreebaseObject> r = api.getInfo(mids);
        for (final FreebaseObject obj : r.values()) {
            System.out.println(obj.toJson());
        }
    }
}
