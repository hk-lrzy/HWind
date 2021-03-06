package org.hklrzy.hwind.servlet;

import com.alibaba.fastjson.JSONObject;
import org.hklrzy.hwind.HWindApplicationContext;
import org.hklrzy.hwind.HWindConfiguration;
import org.hklrzy.hwind.HWindHandlerAdapter;
import org.hklrzy.hwind.HWindModelAndView;
import org.hklrzy.hwind.handler.RequestMappingHandlerAdapter;
import org.hklrzy.hwind.interceptor.HWindInterceptorChain;
import org.hklrzy.hwind.method.annotation.ResponseBodyReturnValueHandler;
import org.hklrzy.hwind.method.support.HWindHandlerExceptionResolver;
import org.hklrzy.hwind.view.JSTLViewResolver;
import org.hklrzy.hwind.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created 2018/6/11.
 * Author ke.hao
 */
public class HWindFrameworkServlet extends HttpServlet {
    private static Logger logger =
            LoggerFactory.getLogger(HWindFrameworkServlet.class);

    private HWindApplicationContext applicationContext;

    private HWindHandlerAdapter handlerAdapter;
    private List<HWindHandlerExceptionResolver> handlerExceptionResolvers;
    private ViewResolver viewResolver;


    @Override

    public void init(ServletConfig servletConfig) throws ServletException {
        /*
            扫描文件以及目录信息
            todo 使用spring.handler来启动配置
         */
        HWindConfiguration windConfiguration = HWindConfiguration.newClassPathConfiguration(servletConfig);

        /*
            整合基本信息成为上下文
         */
        applicationContext = HWindApplicationContext.getApplicationContext();
        applicationContext.init(windConfiguration, servletConfig.getServletContext());
        handlerAdapter = new RequestMappingHandlerAdapter(new ResponseBodyReturnValueHandler());
        viewResolver = new JSTLViewResolver();

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    /**
     * 请求分发中心
     * <p>
     * 通过对请求进行解析然后到context中查找相关的映射，如果找的对应的请求，则调用方法：
     * 1. 希望实现对方法参数的注入 todo
     * 且将返回值进行处理
     * 返回值处理方法：
     * 1>.页面的跳转 todo
     * 2>.实现类似于response的定制化返回方法 todo
     * 2. 融合spring-context spring-web spring-beans这几个基本的部分
     * 1>. 可以省去创建bean的过程，本身bean已经在spring的顶级容器中生成，mvc模块的作用就是在启动的时候解析相应的注解，通过容器中的bean做映射 todo
     * </p>
     *
     * @param request
     * @param response
     */
    private void doDispatch(HttpServletRequest request, HttpServletResponse response) {

        HWindInterceptorChain chainHandler = getHandler(request);

        Exception catchException = null;
        HWindModelAndView mv = null;
        try {
            //todo 404
            if (chainHandler == null || chainHandler.getHandler() == null) {
                noHandlerFound(request, response);
                return;
            }
            chainHandler.applyPreInterceptor(request, response);

            Object handler = chainHandler.getHandler();

            HWindHandlerAdapter handlerAdapter = getHandlerAdapter(handler);

            mv = handlerAdapter.handle(request, response, handler);

            chainHandler.applyPostInterceptor(request, response);
        } catch (Exception e) {
            logger.error("do dispatch failed");
            catchException = e;
        }
        processResponseWithException(request, response, mv, catchException);
    }

    private void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }


    /**
     * todo 怎么处理view
     *
     * @param request
     * @param response
     * @param mv
     * @param catchException
     */
    private void processResponseWithException(HttpServletRequest request, HttpServletResponse response, HWindModelAndView mv, Exception catchException) {
        if (catchException != null) {
            if (!mv.isCleared()) {
                try {
                    render(request, response, mv);
                } catch (Exception e) {
                    logger.error("failed render view with mv [{}]", JSONObject.toJSONString(mv));
                }
            }
        } else {

        }
    }

    private void render(HttpServletRequest request, HttpServletResponse response, HWindModelAndView mv) throws Exception {
        viewResolver.resolver(request, response, mv);
    }

    /**
     * 获取HWindInterceptorChain
     *
     * @param request
     * @return
     */
    private HWindInterceptorChain getHandler(HttpServletRequest request) {
        return applicationContext.getHandler(request);
    }

    public HWindHandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapter;
    }

    @Override
    public void destroy() {

    }

    public List<HWindHandlerExceptionResolver> getHandlerExceptionResolvers() {
        return handlerExceptionResolvers;
    }

    public void setHandlerExceptionResolvers(List<HWindHandlerExceptionResolver> handlerExceptionResolvers) {
        this.handlerExceptionResolvers = handlerExceptionResolvers;
    }
}
