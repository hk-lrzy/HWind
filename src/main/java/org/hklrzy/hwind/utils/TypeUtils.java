package org.hklrzy.hwind.utils;

import com.google.common.primitives.Primitives;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created 2018/6/24.
 * Author ke.hao
 */
public class TypeUtils {


    public static boolean isStringOrWrapperType(Class<?> clazz) {
        return Primitives.isWrapperType(Primitives.wrap(clazz)) || String.class.equals(clazz);
    }

    public static boolean isBoolWrapperType(Class<?> clazz) {
        return isStringOrWrapperType(clazz) &&
                Boolean.class.equals(Primitives.wrap(clazz));

    }

    public static <T> Object[] listToArray(List<T> params) {
        Object[] array = new Object[params.size()];
        int index = 0;
        for (Object param : params) {
            array[index++] = param;
        }
        return array;
    }

    public static Object[] mapToArray(Map<String, Object> param) {
        Object[] array = new Object[param.size()];
        int index = 0;
        for (Iterator iterator = param.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry<String, Object>) iterator.next();
            array[index++] = entry.getValue();
        }
        return array;
    }

}
