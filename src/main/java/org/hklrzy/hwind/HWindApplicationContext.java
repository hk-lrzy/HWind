package org.hklrzy.hwind;

import org.hklrzy.hwind.channel.HChannelContext;
import org.hklrzy.hwind.interceptor.HInterceptorContext;
import org.hklrzy.hwind.interceptor.HWindInterceptorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class HWindApplicationContext<T> {
    private static Logger logger =
            LoggerFactory.getLogger(HWindApplicationContext.class);

    private static HWindApplicationContext applicationContext;
    private static final Object lock = new Object();
    private HWindConfiguration configuration;
    private HInterceptorContext interceptorContext;
    private ServletContext servletContext;
    private WebApplicationContext webApplicationContext;
    private HChannelContext channelContext;


    private HWindApplicationContext() {
    }

    public static HWindApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            synchronized (lock) {
                if (applicationContext == null) {
                    applicationContext = new HWindApplicationContext();
                }
            }
        }
        return applicationContext;
    }

    public void init(HWindConfiguration configuration, ServletContext servletContext) {
        this.configuration = configuration;
        this.servletContext = servletContext;
        this.webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        initFramework(this);
    }

    /**
     * 初始化框架
     */
    private void initFramework(HWindApplicationContext windApplicationContext) {

        /*
         * 初始化一些文本信息
         */
        initInterceptorContext(windApplicationContext);
        initChannelContext();

    }

    private void initInterceptorContext(HWindApplicationContext windApplicationContext) {
        interceptorContext = HInterceptorContext.getInterceptorContext(windApplicationContext);
        interceptorContext.initInterceptorContext(configuration);
    }

    private void initChannelContext() {
        channelContext = HChannelContext.getHChannelContext();
        channelContext.init(this);
    }


    public HWindInterceptorChain getHandler(HttpServletRequest request) {
        String lookup = getLookUpPathForRequest(request);

        Object channel = getHandlerInternal(lookup);

        getChannelInterceptorChain(channel, request);

        return null;
    }

    private HWindInterceptorChain getChannelInterceptorChain(Object channel, HttpServletRequest request) {
        String lookupPath = getLookUpPathForRequest(request);
        return null;
    }

    protected Object getHandlerInternal(String lookup) {
        HWindChannel channel = channelContext.getChannel(lookup);
        if (channel == null) {
            channel = channelContext.getDefaultChannel();
        }
        return channel;
    }


    public String getLookUpPathForRequest(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * 解析request请求的uri 查找相应的Channel
     *
     * @param request
     * @return
     */
    private String parse(HttpServletRequest request) {
        return request.getRequestURI();
    }

    public HWindConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(HWindConfiguration configuration) {
        this.configuration = configuration;
    }

    public HInterceptorContext getInterceptorContext() {
        return interceptorContext;
    }

    public void setInterceptorContext(HInterceptorContext interceptorContext) {
        this.interceptorContext = interceptorContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    public void setWebApplicationContext(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    public HChannelContext getChannelContext() {
        return channelContext;
    }

    public void setChannelContext(HChannelContext channelContext) {
        this.channelContext = channelContext;
    }


}
