package com.longshihan.require.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

/**
 * @author Administrator
 * @time 2016/8/1 16:28
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class PreferencesUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";
    public static final String TAG = PreferencesUtils.class.getSimpleName();

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */
    public static void setObject(Context context , String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getValue(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }
        return null;
    }

    public static void setObject(Context context , Object o){
        Field[] fields = o.getClass().getDeclaredFields();
        SharedPreferences sp = context.getSharedPreferences(o.getClass().getName(), 0);
        SharedPreferences.Editor editor = sp.edit();

        for(int i = 0; i < fields.length; ++i) {
            if(!isParcelableCreator(fields[i])) {
                Class type = fields[i].getType();
                String name = fields[i].getName();
                Object e;
                if(isSingle(type)) {
                    try {
                        if(type != Character.TYPE && !type.equals(String.class)) {
                            if(!type.equals(Integer.TYPE) && !type.equals(Short.class)) {
                                if(type.equals(Double.TYPE)) {
                                    editor.putLong(name, Double.doubleToLongBits(fields[i].getDouble(o)));
                                } else if(type.equals(Float.TYPE)) {
                                    editor.putFloat(name, fields[i].getFloat(o));
                                } else if(type.equals(Long.TYPE) && !name.equals("serialVersionUID")) {
                                    editor.putLong(name, fields[i].getLong(o));
                                } else if(type.equals(Boolean.TYPE)) {
                                    editor.putBoolean(name, fields[i].getBoolean(o));
                                }
                            } else {
                                editor.putInt(name, fields[i].getInt(o));
                            }
                        } else {
                            e = fields[i].get(o);
                            editor.putString(name, null == e?null:e.toString());
                        }
                    } catch (IllegalAccessException var14) {
                        LogUtils.e(TAG, var14);
                    } catch (IllegalArgumentException var15) {
                        LogUtils.e(TAG, var15);
                    }
                } else if(isObject(type)) {
                    try {
                        e = fields[i].get(o);
                        if(null != e) {
                            setObject(context, e);
                        } else {
                            try {
                                setObject(context, fields[i].getClass().newInstance());
                            } catch (InstantiationException var11) {
                                LogUtils.e(TAG, var11);
                            }
                        }
                    } catch (IllegalArgumentException var12) {
                        LogUtils.e(TAG, var12);
                    } catch (IllegalAccessException var13) {
                        LogUtils.e(TAG, var13);
                    }
                } else {
                    try {
                        e = fields[i].get(o);
                        if(null != e) {
                            editor.putString(name, JsonHelper.toJSON(e));
                        }
                    } catch (IllegalAccessException var10) {
                        LogUtils.e(TAG, var10);
                    }
                }
            }
        }

        editor.apply();

    }

    public static <T> T getObject(Context context, Class<T> clazz) {
        Object o = null;

        try {
            o = clazz.newInstance();
        } catch (InstantiationException var14) {
            LogUtils.e(TAG, var14);
            return (T) o;
        } catch (IllegalAccessException var15) {
            LogUtils.e(TAG, var15);
            return (T) o;
        }

        Field[] fields = clazz.getDeclaredFields();
        SharedPreferences sp = context.getSharedPreferences(clazz.getName(), 0);

        for(int i = 0; i < fields.length; ++i) {
            if(!isParcelableCreator(fields[i])) {
                Class type = fields[i].getType();
                String name = fields[i].getName();
                String var18;
                if(isSingle(type)) {
                    try {
                        fields[i].setAccessible(true);
                        if(type != Character.TYPE && !type.equals(String.class)) {
                            if(!type.equals(Integer.TYPE) && !type.equals(Short.class)) {
                                if(type.equals(Double.TYPE)) {
                                    fields[i].setDouble(o, Double.longBitsToDouble(sp.getLong(name, 0L)));
                                } else if(type.equals(Float.TYPE)) {
                                    fields[i].setFloat(o, sp.getFloat(name, 0.0F));
                                } else if(type.equals(Long.TYPE)) {
                                    fields[i].setLong(o, sp.getLong(name, 0L));
                                } else if(type.equals(Boolean.TYPE)) {
                                    fields[i].setBoolean(o, sp.getBoolean(name, false));
                                }
                            } else {
                                fields[i].setInt(o, sp.getInt(name, 0));
                            }
                        } else {
                            var18 = sp.getString(name, (String)null);
                            if(null != var18) {
                                fields[i].set(o, var18);
                            }
                        }
                    } catch (IllegalAccessException var16) {
                        LogUtils.e(TAG, var16);
                    } catch (IllegalArgumentException var17) {
                        LogUtils.e(TAG, var17);
                    }
                } else if(isObject(type)) {
                    Object tempValue = getObject(context, fields[i].getType());
                    if(null != tempValue) {
                        fields[i].setAccessible(true);

                        try {
                            fields[i].set(o, tempValue);
                        } catch (IllegalArgumentException var12) {
                            LogUtils.e(TAG, var12);
                        } catch (IllegalAccessException var13) {
                            LogUtils.e(TAG, var13);
                        }
                    }
                } else {
                    var18 = sp.getString(name, (String)null);
                    if(!TextUtils.isEmpty(var18)) {
                        Object tempObj = null;
                        if(isArray(type)) {
                            tempObj = JsonHelper.parseArray(var18, type);
                        } else if(isCollection(type)) {
                            tempObj = JsonHelper.parseCollection(var18, type, JsonHelper.getGeneric(fields[i]));
                        } else if(isMap(type)) {
                            tempObj = JsonHelper.parseCollection(var18, type, JsonHelper.getGeneric(fields[i]));
                        }

                        if(null != tempObj) {
                            fields[i].setAccessible(true);

                            try {
                                fields[i].set(o, tempObj);
                            } catch (IllegalAccessException var11) {
                                LogUtils.e(TAG, var11);
                            }
                        }
                    }
                }
            }
        }

        return (T) o;
    }


    private static boolean isParcelableCreator(Field field) {
        return Modifier.toString(field.getModifiers()).equals("public static final") && "CREATOR".equals(field.getName());
    }

    private static boolean isObject(Class<?> clazz) {
        return clazz != null && !isSingle(clazz) && !isArray(clazz) && !isCollection(clazz) && !isMap(clazz);
    }

    private static boolean isSingle(Class<?> clazz) {
        return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
    }

    public static boolean isBoolean(Class<?> clazz) {
        return clazz != null && (Boolean.TYPE.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz));
    }

    public static boolean isNumber(Class<?> clazz) {
        return clazz != null && (Byte.TYPE.isAssignableFrom(clazz) || Short.TYPE.isAssignableFrom(clazz) || Integer.TYPE.isAssignableFrom(clazz) || Long.TYPE.isAssignableFrom(clazz) || Float.TYPE.isAssignableFrom(clazz) || Double.TYPE.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz));
    }

    public static boolean isString(Class<?> clazz) {
        return clazz != null && (String.class.isAssignableFrom(clazz) || Character.TYPE.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz));
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

    public static void clearSettings(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().clear().apply();
    }

    public static void clearSettings(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(name, 0);
        sp.edit().clear().apply();
    }

    public static void clearObject(Context context, Class clazz) {
        Field[] fields = clazz.getFields();

        for(int i = 0; i < fields.length; ++i) {
            Class type = fields[i].getType();
            if(isObject(type)) {
                clearObject(context, type);
            }
        }

        clearSettings(context, clazz.getName());
    }
}
