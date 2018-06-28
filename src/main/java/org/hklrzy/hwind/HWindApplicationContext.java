package org.hklrzy.hwind;

import org.hklrzy.hwind.channel.HChannelContext;
import org.hklrzy.hwind.interceptor.HInterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class HWindApplicationContext {
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
        initFramework();
    }

    /**
     * 初始化框架规则
     */
    private void initFramework() {

        /*
         * 初始化拦截器配置
         */
        interceptorContext = HInterceptorContext.getInterceptorContext();
        interceptorContext.initInterceptorContext(configuration);
        channelContext = HChannelContext.getHChannelContext();
        channelContext.init(this);
    }

    public HWindContext createContext(HttpServletRequest request, HttpServletResponse response) {
        String uri = parse(request);
        logger.info("HWind parse uri [ {} ] from request", request.getRequestURI());
        HWindChannel channel = channelContext.getChannel(uri);
        return channel == null ? createDefaultContext(request, response) : createContext(request, response, channel);
    }

    private HWindContext createContext(HttpServletRequest request, HttpServletResponse response, HWindChannel channel) {
        return new HWindContext(request, response, channel);
    }

    private HWindContext createDefaultContext(HttpServletRequest request, HttpServletResponse response) {
        return new HWindContext(request, response, null);
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
