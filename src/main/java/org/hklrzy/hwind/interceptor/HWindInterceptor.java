package org.hklrzy.hwind.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created 2018/1/11.
 * Author ke.hao
 * HWind 框架的拦截器
 */
public interface HWindInterceptor {

    /**
     * 在调用具体handler之前会调用的方法
     * 在执行过程中会按照 interceptor的定义顺序依次执行preHandler(request,response)
     * 1.todo 指定 interceptor的顺序order字段
     *
     * @param request
     * @param response
     * @return 如果在调用某一次interceptor失败之后 拦截器链会终止执行。
     * @throws Exception
     */
    boolean preHandler(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 在调用具体方法之后会调用的方法
     * 在之后过程中会按照 interceptor的定义顺序一次执行postHandler(request,response)
     *
     * @param request
     * @param response
     * @throws Exception
     */
    void postHandler(HttpServletRequest request, HttpServletResponse response) throws Exception;


}
