package org.hklrzy.hwind.handler;

import com.google.common.collect.Lists;
import org.hklrzy.hwind.HWindChannel;
import org.hklrzy.hwind.HWindHandlerAdapter;
import org.hklrzy.hwind.HWindModelAndView;
import org.hklrzy.hwind.method.support.HWindReturnValueHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created 2018/7/23.
 * Author ke.hao
 */
public class RequestMappingHandlerAdapter implements HWindHandlerAdapter {

    private List<HWindReturnValueHandler> returnValueHandlers;

    public RequestMappingHandlerAdapter(HWindReturnValueHandler returnValueHandler) {
        this(Lists.newArrayList(returnValueHandler));
    }

    public RequestMappingHandlerAdapter(List<HWindReturnValueHandler> returnValueHandlers) {
        this.returnValueHandlers = returnValueHandlers;
    }

    public boolean supports(Object handler) {
        return handler instanceof HWindChannel;
    }

    @Override
    public HWindModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HWindChannel channel = (HWindChannel) handler;

        Object returnValue = channel.invoke(request, response);

        return processReturnValue(request, response, returnValue, channel);
    }


    private HWindModelAndView processReturnValue(HttpServletRequest request, HttpServletResponse response, Object returnValue, HWindChannel channel) throws Exception {
        HWindModelAndView modelAndView = new HWindModelAndView();
        for (HWindReturnValueHandler returnValueHandler : returnValueHandlers) {
            if (returnValueHandler.supportsReturnType(channel)) {
                returnValueHandler.handleReturnValue(returnValue, channel, modelAndView, request, response);
            }
        }
        return modelAndView;
    }

    public List<HWindReturnValueHandler> getReturnValueHandlers() {
        return returnValueHandlers;
    }

    public void setReturnValueHandlers(List<HWindReturnValueHandler> returnValueHandlers) {
        this.returnValueHandlers = returnValueHandlers;
    }
}
