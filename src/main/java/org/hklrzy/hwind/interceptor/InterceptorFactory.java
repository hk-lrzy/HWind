package org.hklrzy.hwind.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class InterceptorFactory {
    private static Logger logger =
            LoggerFactory.getLogger(InterceptorFactory.class);
    private static InterceptorFactory interceptorFactory;

    public static InterceptorFactory getInterceptorFactory() {
        if (interceptorFactory == null) {
            interceptorFactory = new InterceptorFactory();
        }
        return interceptorFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInterceptor(String className, Class<T> requiredType) {
        T bean = null;
        try {
            Class<?> clazz = Class.forName(className);
            if (requiredType.isAssignableFrom(clazz)) {
                bean = (T) clazz.newInstance();
            }
        } catch (Exception e) {
            logger.error("instance class with name [{}] and type [{}] failed", className, requiredType);
        }
        return bean;
    }
}
