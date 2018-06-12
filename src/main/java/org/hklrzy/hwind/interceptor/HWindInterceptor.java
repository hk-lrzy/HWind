package org.hklrzy.hwind.interceptor;

import org.hklrzy.hwind.HWindApplicationContext;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public interface HWindInterceptor {

    void init(HWindApplicationContext applicationContext);

    void intercept(HWindInterceptorChain chain) throws Exception;

    void destroy();
}
