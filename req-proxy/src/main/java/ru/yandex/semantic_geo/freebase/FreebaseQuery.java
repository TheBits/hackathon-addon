package ru.yandex.semantic_geo.freebase;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rasifiel
 * Date: 06.10.12
 * Time: 15:00
 */
public class FreebaseQuery {

    private final List<String> req = new LinkedList<String>();

    public FreebaseQuery() {
    }

    public FreebaseQuery add(final String param, final String value) {
        req.add(param + "=" + value);
        return this;
    }

    public String toQuery() {
        return StringUtils.join(req, '&');
    }

}
