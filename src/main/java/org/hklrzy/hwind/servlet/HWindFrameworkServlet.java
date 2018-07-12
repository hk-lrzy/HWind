package org.hklrzy.hwind.servlet;

import org.hklrzy.hwind.HWindApplicationContext;
import org.hklrzy.hwind.HWindChannel;
import org.hklrzy.hwind.HWindConfiguration;
import org.hklrzy.hwind.HWindContext;
import org.hklrzy.hwind.annotation.Channel;
import org.hklrzy.hwind.channel.HChannelContext;
import org.hklrzy.hwind.interceptor.HWindInterceptorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created 2018/6/11.
 * Author ke.hao
 */
public class HWindFrameworkServlet extends HttpServlet {
    private static Logger logger =
            LoggerFactory.getLogger(HWindFrameworkServlet.class);

    private static final String CONFIG_NAME = "config";
    private HWindApplicationContext applicationContext;


    @Override
    public void init(ServletConfig config) throws ServletException {
        String configFilePath = config.getInitParameter(CONFIG_NAME);

        /*
            扫描文件以及目录信息
            todo 使用spring.handler来启动配置
         */
        HWindConfiguration hWindConfiguration = HWindConfiguration.newClassPathConfiguration(configFilePath);

        /*
            整合基本信息成为上下文
         */
        applicationContext = HWindApplicationContext.getApplicationContext();
        applicationContext.init(hWindConfiguration, config.getServletContext());
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

        Exception catchException;
        try {
            chainHandler.applyPreInterceptor(request, response);

            Object handler = chainHandler.getHandler();

            HWindChannel channel = (HWindChannel) handler;

            Object invoke = channel.invoke(request);

            chainHandler.applyPostInterceptor(request, response);
        } catch (Exception e) {
            catchException = e;
        }


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

    @Override
    public void destroy() {

    }
}
