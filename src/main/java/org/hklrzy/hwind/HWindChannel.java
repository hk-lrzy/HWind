package org.hklrzy.hwind;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.hklrzy.hwind.constants.HWindConstants;
import org.hklrzy.hwind.utils.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    private List<String> parameterNames;

    private List<Class<?>> parameterTypes;

    private List<String> interceptorRefNames;

    private List<String> requireParams;

    private Pack pack;


    @SuppressWarnings("all")
    public Object invoke(HWindContext context) {
        try {
            Map<String, String[]> parameterMap = context.getRequest().getParameterMap();
            Class<?> proxyClazz = Class.forName(className);
            Method method = proxyClazz.getMethod(methodName, (Class<?>[]) TypeUtils.listToArray(parameterTypes));
            Object proxy = proxyClazz.newInstance();
            return method.invoke(proxy, getParameterArray(method, context.getRequest().getParameterMap()));
        } catch (Exception e) {
            logger.error("HWind invoke channel [ {} ] and class name [ {} ] failed", name, className);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 通过invoke的方法中的parameter从request参数中获取对于名称的参数并且填充到指定类当中
     *
     * @param method
     * @param parametersMap
     * @return
     */
    private List<Object> getParameterArray(Method method, Map<String, String[]> parametersMap) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Parameter[] parameters = method.getParameters();
        List<Object> parameterLists = Lists.newArrayList();
        for (Parameter parameter : parameters) {
            String parameterName = parameter.getName();
            Class<?> parameterType = parameter.getType();
            if (TypeUtils.isStringOrWrapperType(parameterType)) {
                parameterLists.add(parametersMap.get(parameterName));
            } else {
                Object paramObject = parameterType.newInstance();
                getParameterArray(paramObject, parameterType, parameterType.getDeclaredFields(), parametersMap, parameterLists);
            }
        }

        return parameterLists;
    }

    private void getParameterArray(Object object, Class<?> clazz, Field[] fields, Map<String, String[]> parametersMap, List<Object> primitiveParameters) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (!CollectionUtils.sizeIsEmpty(fields)) {
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                String name = field.getName();
                if (TypeUtils.isStringOrWrapperType(fieldType)) {
                    Method method = clazz.getMethod(getTypeName(fieldType, name), fieldType);
                    if (method != null)
                        method.invoke(object, parametersMap.get(name));
                } else {
                    Object fieldObject = fieldType.newInstance();
                    getParameterArray(fieldObject, fieldType, fieldType.getDeclaredFields(), parametersMap, primitiveParameters);
                }
            }
            primitiveParameters.add(object);
        }
    }

    public String getCanonicalName() {
        StringBuilder stringBuilder = new StringBuilder(pack.getNamespace());
        if (!pack.getNamespace().endsWith("/")) {
            stringBuilder.append("/");
        }
        return stringBuilder.append(name).toString();
    }

    private String getTypeName(Class<?> fieldType, String fieldName) {
        if (Strings.isNullOrEmpty(fieldName)) {
            throw new IllegalArgumentException();
        }
        if (TypeUtils.isBoolWrapperType(fieldType)) {
            if (fieldName.toUpperCase().startsWith("IS")) {
                fieldName = fieldName.substring(2);
            }
        }
        String firstChar = fieldName.substring(0, 1);
        String lastChars = fieldName.substring(1);
        return HWindConstants.SET_METHOD_PREFIX + firstChar.toUpperCase() + lastChars;

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

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public void setParameterNames(List<String> parameterNames) {
        this.parameterNames = parameterNames;
    }

    public List<Class<?>> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<Class<?>> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
