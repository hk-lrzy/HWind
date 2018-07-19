package org.hklrzy.hwind;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hklrzy.hwind.constants.HWindConstants;
import org.hklrzy.hwind.utils.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
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

    private String namespace;

    private String name;

    private String className;

    private String methodName;

    private String[] parameterNames;

    private Class<?>[] parameterTypes;

    private List<String> interceptorRefNames;

    private List<String> requireParams;

    private Object channelHandler;

    private Pack pack;

    @SuppressWarnings("all")
    public Object invoke(HttpServletRequest request) {
        try {
            Class<?> handlerClass = channelHandler == null ? Class.forName(className) : channelHandler.getClass();
            channelHandler = channelHandler == null ? handlerClass.newInstance() : channelHandler;

            Map<String, String[]> parameterMap = request.getParameterMap();

            Method method = handlerClass.getMethod(methodName, parameterTypes);

            List<Object> parameterList = getParameterArray(method, request.getParameterMap());

            return method.invoke(channelHandler, TypeUtils.listToArray(parameterList));

        } catch (Exception e) {
            logger.error("HWind invoke channel [{}] and class name [{}] failed", name, className, e);
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
        StringBuilder stringBuilder = new StringBuilder();
        String packNamespace = pack.getNamespace();

        if (StringUtils.isNotEmpty(packNamespace) && !("/".equals(packNamespace))) {
            stringBuilder.append(packNamespace);
        }

        if (Strings.isNullOrEmpty(namespace)) {
            throw new IllegalArgumentException(String.format("channel with class name [%s] namespace is empty", this.className));
        }
        if ('/' != (namespace.charAt(0))) {
            stringBuilder.append("/");
        }
        stringBuilder.append(this.namespace);
        return stringBuilder.toString();
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

    public String[] getParameterNames() {
        return parameterNames;
    }

    public void setParameterNames(String[] parameterNames) {
        this.parameterNames = parameterNames;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object getChannelHandler() {
        return channelHandler;
    }

    public void setChannelHandler(Object channelHandler) {
        this.channelHandler = channelHandler;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
