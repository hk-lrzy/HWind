package org.hklrzy.hwind;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class HWindChannel {
    private static Logger logger =
            LoggerFactory.getLogger(HWindChannel.class);

    private String name;

    private String className;

    private String methodName;

    private List<Class<?>> parameterTypes;

    private List<String> interceptorRefNames;

    private List<String> requireParams;

    private Pack pack;


    @SuppressWarnings("all")
    public Object invoke(HWindContext context) {
        try {
            Map<String, String[]> parameterMap = context.getRequest().getParameterMap();
            Class<?> proxyClazz = Class.forName(className);
            Method method = proxyClazz.getMethod(methodName);
            Object proxy = proxyClazz.newInstance();
            return method.invoke(proxy, getParameterArray(method, context.getRequest().getParameterMap()));
        } catch (Exception e) {
            logger.error("hChannel invoke failed");
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<Object> getParameterArray(Method method, Map<String, String[]> parametersMap) {
        Parameter[] parameters = method.getParameters();
        List<Object> parameterLists = Lists.newArrayList();
        for (Parameter parameter : parameters) {
            String parameterName = parameter.getName();
            Class<?> parameterType = parameter.getType();
            if (parameterType.isPrimitive()) {
                parameterLists.add(parametersMap.get(parameterName));
            }

        }
        return parameterLists;
    }


    public String getCanonicalName() {
        StringBuilder stringBuilder = new StringBuilder(pack.getNamespace());
        if (!pack.getNamespace().endsWith("/")) {
            stringBuilder.append("/");
        }
        return stringBuilder.append(name).toString();
    }

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
