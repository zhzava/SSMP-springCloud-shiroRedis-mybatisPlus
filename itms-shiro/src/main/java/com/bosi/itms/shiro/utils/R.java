package com.bosi.itms.shiro.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 * <br>Created by Admin on 2017/5/5.
 * <br>星期五 at 15:32.
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    private static final String type_fail = "fail";
    private static final String type_succ = "success";

    private R() {
        put("code", 200);
        put("type", type_succ);
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        r.put("type", type_fail);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }
    public static R ok(int code,String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok(Object obj){
        R r = new R();
        r.put("msg",obj);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
