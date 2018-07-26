package org.hklrzy.hwind.utils;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 反射的工具类
 * Created 2018/7/24.
 * @author ke.hao
 */
public class RefectionUtils {
    private static Logger logger =
            LoggerFactory.getLogger(RefectionUtils.class);


    public static Class<?> getBeanClass(String className) {
        Preconditions.checkNotNull(className);
        try {
            return Class.forName(className);
        } catch (Exception e) {
            logger.error("get bean class failed with class name [{}]", className, e);
            throw new IllegalArgumentException(String.format("fail to get bean class with class name [%s]", className));
        }
    }

    public static Method getClassMethod(Class<?> clazz, String method, Class... parameters) {
        try {
            return clazz.getMethod(method, parameters);
        } catch (Exception e) {
            logger.error("get method with clazz [{}] and method name [{}] and parameters [{}] failed", clazz, method, parameters);
            throw new IllegalArgumentException();
        }
    }
}
