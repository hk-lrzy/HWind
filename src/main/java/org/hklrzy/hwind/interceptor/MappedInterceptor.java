package org.hklrzy.hwind.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: haoke
 * Created: 2018/7/8
 */
public class MappedInterceptor implements HWindInterceptor {

    private final String[] includePatterns;

    private final String[] excludePatterns;

    private final HWindInterceptor interceptor;

    public MappedInterceptor(String[] includePatterns, HWindInterceptor interceptor) {
        this(includePatterns, null, interceptor);
    }

    public MappedInterceptor(String[] includePatterns, String[] excludePatterns, HWindInterceptor interceptor) {
        this.includePatterns = includePatterns;
        this.excludePatterns = excludePatterns;
        this.interceptor = interceptor;
    }


    @Override
    public boolean preHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return false;
    }

    @Override
    public void postHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}
