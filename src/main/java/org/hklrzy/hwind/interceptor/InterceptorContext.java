package org.hklrzy.hwind.interceptor;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.hklrzy.hwind.HWindConfiguration;
import org.hklrzy.hwind.Pack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class InterceptorContext {

    private static Logger logger =
            LoggerFactory.getLogger(InterceptorContext.class);
    private static final String DEFAULT_NAMESPACE = "/";
    private static InterceptorContext interceptorContext;
    private InterceptorFactory interceptorFactory;
    private Map<String, Map<String, HWindInterceptor>> interceptors = Maps.newHashMap();


    private InterceptorContext() {
        this.interceptorFactory = InterceptorFactory.getInterceptorFactory();
    }

    public static InterceptorContext getInterceptorContext() {
        if (interceptorContext == null) {
            interceptorContext = new InterceptorContext();
        }
        return interceptorContext;
    }

    public void initInterceptorContext(HWindConfiguration configuration) {
        Preconditions.checkNotNull(configuration, "configuration is null");

        /*
        初始化拦截器
         */
        initInterceptors(configuration);


    }

    /**
     * 初始化拦截器配置
     *
     * @param configuration 拦截器的配置信息
     */
    private void initInterceptors(HWindConfiguration configuration) {
        initInterceptors(DEFAULT_NAMESPACE, configuration.getInterceptorDefines());

        List<Pack> packs = configuration.getPacks();
        for (Pack pack : packs) {
            initInterceptors(pack.getNamespace(), pack.getInterceptorDefines());
        }


    }

    private void initInterceptors(String namespace, List<InterceptorDefine> interceptors) {
        if (interceptors == null || interceptors.size() == 0) {
            logger.info("HWind has no interceptors");
            return;
        }
        Map<String, HWindInterceptor> interceptorMap = this.interceptors.get(namespace);
        if (interceptorMap == null) {
            interceptorMap = Maps.newHashMap();
            this.interceptors.put(namespace, interceptorMap);
        }

        for (InterceptorDefine interceptorDefine : interceptors) {
            HWindInterceptor interceptor = interceptorFactory.getInterceptor(interceptorDefine);
            interceptorMap.put(interceptorDefine.getName(), interceptor);
        }
    }

    public HWindInterceptor getHWindInterceptor(String namespace, String name) {
        if (Strings.isNullOrEmpty(namespace) || Strings.isNullOrEmpty(name)) {
            logger.error("HWind interceptor's name or namespace can't be null or empty");
            throw new IllegalArgumentException(String.format("illegal argument to get HWind interceptor with name [ %s ] and [ %s ]", name, namespace));
        }
        return interceptors.get(namespace).get(name);

    }


}
