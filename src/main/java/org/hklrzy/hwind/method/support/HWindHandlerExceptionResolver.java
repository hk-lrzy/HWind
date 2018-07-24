package org.hklrzy.hwind.method.support;

import org.hklrzy.hwind.HWindModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created 2018/7/24.
 * Author ke.hao
 */
public interface HWindHandlerExceptionResolver {

    HWindModelAndView HandlerExceptionResolver(HttpServletRequest request, HttpServletResponse response, Exception ex);
}
