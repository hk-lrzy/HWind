package org.hklrzy.hwind.utils;

import com.google.common.primitives.Primitives;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
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
        if (CollectionUtils.isEmpty(params)) {
            return new Object[0];
        }
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

    public static String[] toStringArray(List<String> params) {
        if (!validate(params)) {
            return new String[0];
        }
        String[] array = new String[params.size()];
        int index = 0;

        for (String param : params) {
            array[index++] = param;
        }
        return array;
    }

    private static boolean validate(Collection<?> collection) {
        return CollectionUtils.isNotEmpty(collection);
    }

}
