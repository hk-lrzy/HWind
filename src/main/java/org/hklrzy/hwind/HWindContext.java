package org.hklrzy.hwind;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created 2018/1/11.
 * Author ke.hao
 * <p>
 * 请求的上下文信息
 * </p>
 */
public class HWindContext {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HWindChannel channel;

    private Object callback;

    private Object result;

    public HWindContext(HttpServletRequest request, HttpServletResponse response, HWindChannel channel) {
        this.request = request;
        this.response = response;
        this.channel = channel;
    }


    public void invoke() {

        this.channel.invoke(this);
    }

    public void doCallBack() {

    }


    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HWindChannel getChannel() {
        return channel;
    }

    public void setChannel(HWindChannel channel) {
        this.channel = channel;
    }
}
