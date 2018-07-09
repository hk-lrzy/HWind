package org.hklrzy.hwind.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: haoke
 * Created: 2018/7/8
 */
public class MappedInterceptor implements HWindInterceptor {


    @Override
    public boolean preHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return false;
    }

    @Override
    public void postHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}
