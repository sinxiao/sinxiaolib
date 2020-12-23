package com.xu.sinxiao.common;

import android.text.TextUtils;

import com.xu.sinxiao.common.logger.Logger;

import java.lang.reflect.Field;

public class ReflectUtils {
    public static Field getField(Class<?> sourceClass, String fieldName, boolean isFindDeclaredField, boolean isSuperFind) {
        Field field = null;

        try {
            field = isFindDeclaredField ? sourceClass.getDeclaredField(fieldName) : sourceClass.getField(fieldName);
        } catch (NoSuchFieldException var9) {
            if (isSuperFind) {
                Class cls = sourceClass.getSuperclass();

                while (field == null && cls != null) {
                    try {
                        field = isFindDeclaredField ? cls.getDeclaredField(fieldName) : cls.getField(fieldName);
                    } catch (NoSuchFieldException var8) {
                        cls = cls.getSuperclass();
                    }
                }
            }
        }

        return field;
    }

    public static <T> T getObjectByFieldName(Object object, String fieldName, Class<T> cls) {
        if (object != null && !TextUtils.isEmpty(fieldName) && cls != null) {
            try {
                Field field = getField(object.getClass(), fieldName, true, true);
                if (field != null) {
                    field.setAccessible(true);
                    return (T) field.get(object);
                } else {
                    return null;
                }
            } catch (Exception var4) {
                Logger.e(var4.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }
}
