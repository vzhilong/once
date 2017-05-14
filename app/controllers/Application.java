package controllers;

import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Router;

import java.util.HashMap;
import java.util.Map;

public class Application extends Controller {

    private static final String SAVED_PREFIX = "saved_%s";

    public static void index() {
        create();
    }

    public static void create() {
        render();
    }

    public static void send(String content) {
        String id = String.valueOf(System.currentTimeMillis());
        String savedKey = String.format(SAVED_PREFIX, id);
        Cache.add(savedKey, content, "1h");

        Map param = new HashMap();
        param.put("id", id);
        String viewUrl = Router.getFullUrl("Application.message", param);

        Map res = new HashMap();
        res.put("success", true);
        res.put("message_id", id);
        res.put("view_url", viewUrl);

        renderJSON(res);
    }

    public static void message(String id) {
        String savedKey = String.format(SAVED_PREFIX, id);
        Object cached = Cache.get(savedKey);
        String content = cached == null ? "" : (String) cached;

        renderText(content);
    }
}