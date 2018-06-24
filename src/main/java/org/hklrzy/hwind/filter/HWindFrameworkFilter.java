package org.hklrzy.hwind.filter;


import org.hklrzy.hwind.HWindApplicationContext;
import org.hklrzy.hwind.HWindConfiguration;
import org.hklrzy.hwind.HWindContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created 2018/1/11.
 * Author ke.hao
 * <p>
 * HWind框架的核心主类用户初始化filter以及url的dispatcher
 * </p>
 */
public class HWindFrameworkFilter implements Filter {
    private static final String CONFIG_NAME = "config";
    private HWindApplicationContext applicationContext;


    public void init(FilterConfig config) throws ServletException {
        String configFilePath = config.getInitParameter(CONFIG_NAME);

        /*
            扫描文件以及目录信息
         */
        HWindConfiguration hWindConfiguration = HWindConfiguration.newClassPathConfiguration(configFilePath);

        /*
            整合基本信息成为上下文
         */
        applicationContext = HWindApplicationContext.getApplicationContext();
        applicationContext.init(hWindConfiguration, config.getServletContext());
    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        dispatcher((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);

    }

    /**
     * 请求分发中心
     * <p>
     * 通过对请求进行解析然后到context中查找相关的映射，如果找的对应的请求，则调用方法：
     * 1. 希望实现对方法参数的注入 todo
     * 且将返回值进行处理
     * 返回值处理方法：
     * 1.页面的跳转 todo
     * 2.实现类似于response的定制化返回方法 todo
     * </p>
     *
     * @param request
     * @param response
     */
    private void dispatcher(HttpServletRequest request, HttpServletResponse response) {
        HWindContext context = applicationContext.createContext(request, response);
        context.invoke();
        context.doCallBack();
    }

    public void destroy() {

    }
}
