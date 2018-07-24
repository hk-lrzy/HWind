package org.hklrzy.hwind;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created 2018/7/23.
 * Author ke.hao
 */
public interface HWindHandlerAdapter {


    HWindModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}