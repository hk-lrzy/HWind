package org.hklrzy.hwind.method.support;

import org.hklrzy.hwind.channel.HWindChannel;
import org.hklrzy.hwind.HWindModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于处理Channel接口返回值的接口
 * <p>
 * Created 2018/7/24.
 * Author ke.hao
 */
public interface HWindReturnValueHandler {


    boolean supportsReturnType(HWindChannel channel);


    void handleReturnValue(Object returnValue, HWindChannel channel, HWindModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
