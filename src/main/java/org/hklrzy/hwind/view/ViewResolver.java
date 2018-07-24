package org.hklrzy.hwind.view;

import org.hklrzy.hwind.HWindModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created 2018/7/24.
 * Author ke.hao
 */
public interface ViewResolver {

    void resolver(HttpServletRequest request, HttpServletResponse response, HWindModelAndView modelAndView) throws Exception;
}
