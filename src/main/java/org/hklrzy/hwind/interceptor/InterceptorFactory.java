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

    public HWindInterceptor getInterceptor(InterceptorDefine interceptorDefine) {
        HWindInterceptor interceptor = null;
        try {
            Class<?> clazz = Class.forName(interceptorDefine.getClassName());
            if (HWindInterceptor.class.isAssignableFrom(clazz)) {
                interceptor = (HWindInterceptor) clazz.newInstance();
            }
        } catch (Exception e) {
            logger.error("class {} newInstance interceptor failed", interceptorDefine.getClassName());
        }
        return interceptor;
    }
}
