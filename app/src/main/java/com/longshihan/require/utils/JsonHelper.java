package com.longshihan.require.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * @author Administrator
 * @time 2016/8/1 16:44
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class JsonHelper {
    public static String toJSON(Object e) {
        JSONStringer js = new JSONStringer();
        serialize(js, e);
        return js.toString();
    }

    private static void serialize(JSONStringer js, @NonNull Object o) {

        Class clazz = o.getClass();
        if (isObject(clazz)) {
            serializeObject(js, o);
        } else if (isArray(clazz)) {
            serializeArray(js, o);
        } else if (isCollection(clazz)) {
            Collection e = (Collection) o;
            serializeCollect(js, e);
        } else if (isMap(clazz)) {
            HashMap e1 = (HashMap) o;
            serializeMap(js, e1);
        } else {
            try {
                js.value(o);
            } catch (JSONException var5) {
                LogUtils.e(TAG, var5);
            }
        }
    }


    private static void serializeArray(JSONStringer js, Object array) {
        try {
            js.array();

            for (int e = 0; e < Array.getLength(array); ++e) {
                Object o = Array.get(array, e);
                serialize(js, o);
            }

            js.endArray();
        } catch (Exception var4) {
            LogUtils.e(TAG, var4);
        }

    }

    private static void serializeCollect(JSONStringer js, Collection<?> collection) {
        try {
            js.array();
            Iterator e = collection.iterator();

            while (e.hasNext()) {
                Object o = e.next();
                serialize(js, o);
            }

            js.endArray();
        } catch (Exception var4) {
            LogUtils.e(TAG, var4);
        }

    }

    private static void serializeObject(JSONStringer js, Object obj) {
        try {
            js.object();
            Field[] e = obj.getClass().getDeclaredFields();
            int len$ = e.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                Field f = e[i$];
                if (!f.getName().equals("serialVersionUID")) {
                    f.setAccessible(true);
                    Object o = f.get(obj);
                    js.key(f.getName());
                    serialize(js, o);
                }
            }

            js.endObject();
        } catch (Exception var7) {
            LogUtils.e(TAG, var7);
        }

    }

    private static void serializeMap(JSONStringer js, Map<?, ?> map) {
        try {
            js.object();
            Iterator it = map.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                js.key((String) entry.getKey());
                serialize(js, entry.getValue());
            }

            js.endObject();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static boolean isSingle(Class<?> clazz) {
        return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
    }

    public static boolean isBoolean(Class<?> clazz) {
        return clazz != null && (Boolean.TYPE.isAssignableFrom(clazz) || Boolean.class
                .isAssignableFrom(clazz));
    }

    public static boolean isNumber(Class<?> clazz) {
        return clazz != null && (Byte.TYPE.isAssignableFrom(clazz) || Short.TYPE.isAssignableFrom
                (clazz) || Integer.TYPE.isAssignableFrom(clazz) || Long.TYPE.isAssignableFrom
                (clazz) || Float.TYPE.isAssignableFrom(clazz) || Double.TYPE.isAssignableFrom
                (clazz) || Number.class.isAssignableFrom(clazz));
    }

    public static boolean isString(Class<?> clazz) {
        return clazz != null && (String.class.isAssignableFrom(clazz) || Character.TYPE
                .isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz));
    }

    private static boolean isObject(Class<?> clazz) {
        return clazz != null && !isSingle(clazz) && !isArray(clazz) && !isCollection(clazz) &&
                !isMap(clazz);
    }

    public static boolean isArray(Class<?> clazz) {
        return clazz != null && clazz.isArray();
    }

    public static boolean isCollection(Class<?> clazz) {
        return clazz != null && Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isMap(Class<?> clazz) {
        return clazz != null && Map.class.isAssignableFrom(clazz);
    }

    public static boolean isList(Class<?> clazz) {
        return clazz != null && List.class.isAssignableFrom(clazz);
    }

    public static Class getGeneric(Field field) {
        Class c = null;
        Type gType = field.getGenericType();
        if(gType instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType)gType;
            Type[] targs = ptype.getActualTypeArguments();
            if(targs != null && targs.length > 0) {
                Type t = targs[0];
                c = (Class)t;
            }
        }

        return c;
    }

    public static <T> T[] parseArray(String jsonString, Class<T> clazz) {
        if(clazz != null && jsonString != null && jsonString.length() != 0) {
            JSONArray jo = null;
            try {
                jo = new JSONArray(jsonString);
            } catch (JSONException var4) {
                LogUtils.e(TAG, var4);
            }
            return isNull(jo)?null:parseArray(jo, clazz);
        } else {
            return null;
        }
    }

    public static <T> T[] parseArray(JSONArray ja, Class<T> clazz) {
        if(clazz != null && !isNull(ja)) {
            int len = ja.length();
            Object[] array = (Object[])((Object[])Array.newInstance(clazz, len));

            for(int i = 0; i < len; ++i) {
                try {
                    JSONObject e = ja.getJSONObject(i);
                    Object o = parseObject(e, clazz);
                    array[i] = o;
                } catch (JSONException var7) {
                    LogUtils.e(TAG, var7);
                }
            }

            return (T[]) array;
        } else {
            return null;
        }
    }

    public static <T> T parseObject(JSONObject jo, Class<T> clazz) {
        if(clazz != null && !isNull(jo)) {
            Object obj = createInstance(clazz);
            if(obj == null) {
                return null;
            } else {
                for(Class forClazz = obj.getClass(); forClazz != Object.class; forClazz = forClazz.getSuperclass()) {
                    Field[] arr$ = forClazz.getDeclaredFields();
                    int len$ = arr$.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        Field f = arr$[i$];
                        if(!"CREATOR".equals(f.getName())) {
                            f.setAccessible(true);
                            setField(obj, f, jo);
                        }
                    }
                }

                return (T) obj;
            }
        } else {
            return null;
        }
    }

    private static void setField(Object obj, Field f, JSONObject jo) {
        String name = f.getName();
        Class clazz = f.getType();

        try {
            JSONArray o2;
            Class e6;
            if(isArray(clazz)) {
                e6 = clazz.getComponentType();
                o2 = jo.optJSONArray(name);
                if(!isNull(o2)) {
                    Object[] o3 = parseArray(o2, e6);
                    f.set(obj, o3);
                }
            } else if(isCollection(clazz)) {
                e6 = getGeneric(f);
                o2 = jo.optJSONArray(name);
                if(!isNull(o2)) {
                    Collection o1 = parseCollection(o2, clazz, e6);
                    f.set(obj, o1);
                }
            } else if(isSingle(clazz)) {
                String e3;
                if(f.getType() == Character.TYPE) {
                    e3 = jo.optString(name);
                    if(e3 == null || "".equals(e3)) {
                        return;
                    }

                    f.setChar(obj, e3.toCharArray()[0]);
                } else if(f.getType().equals(Integer.TYPE)) {
                    int e1 = jo.optInt(name);
                    f.setInt(obj, e1);
                } else if(clazz.equals(Double.TYPE)) {
                    double e2 = jo.optDouble(name);
                    if(Double.isNaN(e2)) {
                        f.setDouble(obj, 0.0D);
                    } else {
                        f.setDouble(obj, e2);
                    }
                } else if(clazz.equals(Float.TYPE)) {
                    e3 = jo.optString(name);
                    if(!"null".equals(e3) && !TextUtils.isEmpty(e3) && !"NULL".equals(e3)) {
                        f.setFloat(obj, Float.valueOf(e3).floatValue());
                    } else {
                        f.setFloat(obj, 0.0F);
                    }
                } else if(clazz.equals(String.class)) {
                    e3 = jo.optString(name);
                    e3 = "null".equals(e3)?"":e3;
                    f.set(obj, e3);
                } else if(clazz.equals(Long.TYPE)) {
                    long e4 = jo.optLong(name);
                    f.set(obj, Long.valueOf(e4));
                } else {
                    Object e5 = jo.opt(name);
                    if(e5 != null) {
                        f.set(obj, e5);
                    }
                }
            } else {
                if(!isObject(clazz)) {
                    throw new Exception("unknow type!");
                }

                JSONObject e = jo.optJSONObject(name);
                if(!isNull(e)) {
                    Object o = parseObject(e, clazz);
                    f.set(obj, o);
                }
            }
        } catch (Exception var8) {
            LogUtils.e(TAG, var8);
        }

    }

    public static <T> Collection<T> parseCollection(JSONArray ja, Class<?> collectionClazz, Class<T> genericType) {
        if(collectionClazz != null && genericType != null && !isNull(ja)) {
            if(collectionClazz == List.class) {
                collectionClazz = ArrayList.class;
            }

            Collection collection = (Collection)createInstance(collectionClazz);

            for(int i = 0; i < ja.length(); ++i) {
                try {
                    Object o;
                    if(isSingle(genericType)) {
                        o = ja.get(i);
                    } else {
                        JSONObject e = ja.getJSONObject(i);
                        o = parseObject(e, genericType);
                    }

                    collection.add(o);
                } catch (JSONException var7) {
                    LogUtils.e(TAG, var7);
                }
            }

            return collection;
        } else {
            return null;
        }
    }
    public static <T> Collection<T> parseCollection(String jsonString, Class<?> collectionClazz, Class<T> genericType) {
        if(collectionClazz != null && genericType != null && jsonString != null && jsonString.length() != 0) {
            JSONArray jo = null;

            try {
                jo = new JSONArray(jsonString);
            } catch (JSONException var5) {
                var5.printStackTrace();
            }

            return isNull(jo)?null:parseCollection(jo, collectionClazz, genericType);
        } else {
            return null;
        }
    }


    private static <T> T createInstance(Class<T> clazz) {
        if(clazz == null) {
            return null;
        } else {
            Object obj = null;

            try {
                obj = clazz.newInstance();
            } catch (Exception var3) {
                LogUtils.e(TAG, var3);
            }

            return (T) obj;
        }
    }


    private static boolean isNull(Object obj) {
        return obj instanceof JSONObject ?JSONObject.NULL.equals(obj):obj == null;
    }

}
