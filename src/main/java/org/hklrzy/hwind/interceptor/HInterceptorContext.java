package org.hklrzy.hwind.interceptor;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
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
public class HInterceptorContext {

    private static Logger logger =
            LoggerFactory.getLogger(HInterceptorContext.class);
    private static final String DEFAULT_NAMESPACE = "/";
    private static HInterceptorContext interceptorContext;
    private InterceptorFactory interceptorFactory;
    private Map<String, Map<String, HWindInterceptor>> interceptors = Maps.newHashMap();


    private HInterceptorContext() {
        this.interceptorFactory = InterceptorFactory.getInterceptorFactory();
    }

    public static HInterceptorContext getInterceptorContext() {
        if (interceptorContext == null) {
            interceptorContext = new HInterceptorContext();
        }
        return interceptorContext;
    }

    public void initInterceptorContext(HWindConfiguration configuration) {
        Preconditions.checkNotNull(configuration, "HWind configuration can't be null");

        initInterceptors(configuration);
        initInterceptorStack(configuration);
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
        if (CollectionUtils.isEmpty(interceptors)) {
            logger.debug("HWind has no interceptors");
            return;
        }
        Map<String, HWindInterceptor> interceptorMap = this.interceptors.computeIfAbsent(namespace, k -> Maps.newHashMap());

        for (InterceptorDefine interceptorDefine : interceptors) {
            HWindInterceptor interceptor = interceptorFactory.getInterceptor(interceptorDefine);
            interceptorMap.put(interceptorDefine.getName(), interceptor);
        }
    }

    private void initInterceptorStack(HWindConfiguration configuration) {
        
    }

    public HWindInterceptor getHWindInterceptor(String namespace, String name) {
        if (Strings.isNullOrEmpty(namespace) || Strings.isNullOrEmpty(name)) {
            logger.error("HWind interceptor's name or namespace can't be null or empty");
            throw new IllegalArgumentException(String.format("illegal argument to get HWind interceptor with name [ %s ] and [ %s ]", name, namespace));
        }
        return interceptors.get(namespace).get(name);

    }


}
