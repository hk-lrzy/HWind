package org.hklrzy.hwind.utils;

import com.google.common.primitives.Primitives;

/**
 * Created 2018/6/24.
 * Author ke.hao
 */
public class TypeCheckUtils {


    public static boolean isStringOrWrapperType(Class<?> clazz) {
        return Primitives.isWrapperType(Primitives.wrap(clazz)) || String.class.equals(clazz);
    }

    public static boolean isBoolWrapperType(Class<?> clazz) {
        return isStringOrWrapperType(clazz) &&
                Boolean.class.equals(Primitives.wrap(clazz));

    }


}
