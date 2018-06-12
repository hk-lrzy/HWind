package org.hklrzy.hwind;

import java.util.List;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class HWindChannel {

    private String name;

    private String className;

    private String methodName;

    private List<String> interceptorRefNames;

    private List<String> requireParams;

    private Pack pack;

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

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public List<String> getInterceptorRefNames() {
        return interceptorRefNames;
    }

    public void setInterceptorRefNames(List<String> interceptorRefNames) {
        this.interceptorRefNames = interceptorRefNames;
    }

    public List<String> getRequireParams() {
        return requireParams;
    }

    public void setRequireParams(List<String> requireParams) {
        this.requireParams = requireParams;
    }
}
