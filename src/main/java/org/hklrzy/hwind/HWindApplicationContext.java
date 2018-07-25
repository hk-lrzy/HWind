package org.hklrzy.hwind;

import com.google.common.collect.Lists;
import org.hklrzy.hwind.channel.HChannelContext;
import org.hklrzy.hwind.channel.HWindChannel;
import org.hklrzy.hwind.interceptor.HInterceptorContext;
import org.hklrzy.hwind.interceptor.HWindInterceptor;
import org.hklrzy.hwind.interceptor.HWindInterceptorChain;
import org.hklrzy.hwind.interceptor.MappedInterceptor;
import org.hklrzy.hwind.mather.PathMather;
import org.hklrzy.hwind.mather.SimplePathMather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    private WebApplicationContext parentContext;
    private HChannelContext channelContext;
    private PathMather pathMather;


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
        this.parentContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.configuration = configuration;
        this.servletContext = servletContext;
        this.pathMather = new SimplePathMather();

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

        return getChannelInterceptorChain(channel, request);

    }

    private HWindInterceptorChain getChannelInterceptorChain(Object channel, HttpServletRequest request) {
        String lookupPath = getLookUpPathForRequest(request);

        List<HWindInterceptor> interceptors = getInterceptorContext().getInterceptors();

        List<HWindInterceptor> mappingInterceptor = Lists.newArrayList();
        interceptors.forEach(interceptor -> {
            if (interceptor instanceof MappedInterceptor) {
                if (pathMather.mather((MappedInterceptor) interceptor, lookupPath)) {
                    mappingInterceptor.add(interceptor);
                }
            } else {
                mappingInterceptor.add(interceptor);
            }
        });
        return new HWindInterceptorChain(channel, mappingInterceptor.toArray(new MappedInterceptor[mappingInterceptor.size()]));
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

    public WebApplicationContext getParentContext() {
        return parentContext;
    }

    public void setParentContext(WebApplicationContext parentContext) {
        this.parentContext = parentContext;
    }

    public HChannelContext getChannelContext() {
        return channelContext;
    }

    public void setChannelContext(HChannelContext channelContext) {
        this.channelContext = channelContext;
    }


}
