package org.hklrzy.hwind;

import org.hklrzy.hwind.channel.ChannelContext;
import org.hklrzy.hwind.interceptor.InterceptorContext;
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
    private InterceptorContext interceptorContext;
    private WebApplicationContext webApplicationContext;
    private ChannelContext channelContext;


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
        //this.servletContext = servletContext;
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
        interceptorContext = InterceptorContext.getInterceptorContext();
        interceptorContext.initInterceptorContext(configuration);
        channelContext = ChannelContext.instance();
        channelContext.init(configuration);
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

}
