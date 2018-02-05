package com.sy.prescription.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sy.prescription.model.Template;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zcy on 2017/3/15.
 */

public class GsonUtil {

    /**
     * 时间格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 创建GSON
     *
     * @return
     */

    public static Gson getGson() {
        return new GsonBuilder().serializeNulls().setDateFormat(DATE_FORMAT).create();
    }

    /**
     * 将对象转化为字符串
     *
     * @param obj
     * @return
     */

    public String Object2Json2(Object obj) {
        return getGson().toJson(obj);
    }

    /**
     * 将对象转化为字符串(泛型实现)
     *
     * @param obj
     * @return
     */

    public static <T> String t2Json2(T t) {
        return getGson().toJson(t);
    }

    /**
     * 将字符转化为对象
     *
     * @param <T>
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T json2T(String jsonString, Class<T> clazz) {
        return getGson().fromJson(jsonString, clazz);
    }

    /**
     * 将字符串数组转化为对象集合
     *
     * @param <T>
     * @param jsonStr
     * @param type
     * @return
     */
    public static <T> List<T> json2Collection(String jsonStr, Type type) {
        return getGson().fromJson(jsonStr, type);
    }

    public static Template fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(Template.class, clazz);
        return gson.fromJson(json, objectType);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {

            @Override
            public Type[] getActualTypeArguments() {
                // TODO Auto-generated method stub
                return args;
            }

            @Override
            public Type getOwnerType() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Type getRawType() {
                // TODO Auto-generated method stub
                return raw;
            }
        };
    }
}
