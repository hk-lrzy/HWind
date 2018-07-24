package org.hklrzy.hwind.method.annotation;

import com.alibaba.fastjson.JSONObject;
import org.hklrzy.hwind.HWindChannel;
import org.hklrzy.hwind.HWindModelAndView;
import org.hklrzy.hwind.method.support.HWindReturnValueHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created 2018/7/24.
 * Author ke.hao
 */
public class ResponseBodyReturnValueHandler implements HWindReturnValueHandler {
    private static final String CONTENT_TYPE = "content-type";
    private static final String JSON_APPLICATION = "application/json";


    @Override
    public boolean supportsReturnType(HWindChannel channel) {
        return channel.getMethod().isAnnotationPresent(ResponseBody.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, HWindChannel channel, HWindModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter writer = response.getWriter();
        response.setHeader(CONTENT_TYPE, JSON_APPLICATION);
        response.setStatus(HttpStatus.OK.value());
        writer.print(JSONObject.toJSONString(returnValue));
        writer.flush();
    }
}
