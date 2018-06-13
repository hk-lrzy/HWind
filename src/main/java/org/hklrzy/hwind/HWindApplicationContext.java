package org.hklrzy.hwind;

import org.hklrzy.hwind.channel.ChannelContext;
import org.hklrzy.hwind.interceptor.InterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private ServletContext servletContext;
    private InterceptorContext interceptorContext;
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
        this.servletContext = servletContext;
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
        String uri = request.getRequestURI();
        HWindChannel channel = channelContext.getChannel(uri);
        HWindContext hWindContext = new HWindContext(request, response, channel);
        return hWindContext;
    }
}
