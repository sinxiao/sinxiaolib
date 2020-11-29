package com.xu.sinxiao.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.xu.sinxiao.common.logger.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonUtils {

    public static <T> T parserBean(String jsonStr, Class<T> clz) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonStr, clz);
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            Logger.e(e, "exception");
            e.printStackTrace();
            return null;
        }
        return t;
    }

    public static <T> T parserBean(String jsonStr, Type type) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonStr, type);
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            Logger.e(e, "exception");
            e.printStackTrace();
            return null;
        }
        return t;
    }

    public static <T> List<T> parserBeans(String jsonStr, Class<T> clz) {
        List<T> list = new ArrayList<T>();
        Type type = new TypeToken<List<JsonObject>>() {
        }.getType();
        try {
            Gson gson = new Gson();
            List<JsonObject> jsonObjs = gson.fromJson(jsonStr, type);
            for (JsonObject jsonObj : jsonObjs) {
                list.add(gson.fromJson(jsonObj, clz));
            }
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            return null;
        }
        return list;
    }

    public static <T> List<T> parserBeans(String jsonStr, Type type) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonStr, type);
        } catch (JsonSyntaxException e) {
            Logger.i(e.getMessage());
            return null;
        }
        return list;
    }

    public static <T> List<Map<String, Object>> parserBeanMaps(String jsonStr) {
        List<Map<String, Object>> listmap = null;
        try {
            Gson gson = new Gson();
            listmap = gson.fromJson(jsonStr, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            return null;
        }
        return listmap;
    }

    public static String toJson(Object obj) {
        Gson g = null;
        try {
            g = new GsonBuilder().serializeNulls().create();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
        return g.toJson(obj);
    }

    public static String toTransferJson(Object obj) {
        Gson g = null;
        try {
            g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
        return g.toJson(obj);
    }

}
