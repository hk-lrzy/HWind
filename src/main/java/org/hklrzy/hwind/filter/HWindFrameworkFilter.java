package org.hklrzy.hwind.filter;


import org.hklrzy.hwind.HWindApplicationContext;
import org.hklrzy.hwind.HWindConfiguration;

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

        HWindConfiguration hWindConfiguration = HWindConfiguration.newClassPathConfiguration(configFilePath);
        applicationContext = HWindApplicationContext.getApplicationContext();
        applicationContext.init(hWindConfiguration, config.getServletContext());
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        applicationContext.createContext(request, response);



    }

    public void destroy() {

    }
}
