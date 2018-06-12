package org.hklrzy.hwind.interceptor;

/**
 * Created 2018/1/11.
 * Author ke.hao
 * <p>
 * 拦截器定义
 * name:拦截器名字
 * className:拦截器类名
 * </p>
 */
public class InterceptorDefine {

    /**
     * interceptor's name
     */
    private String name;

    /**
     * the class mapping of the interceptor
     */
    private String className;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
