package org.hklrzy.hwind.interceptor;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.hklrzy.hwind.HWindApplicationContext;
import org.hklrzy.hwind.HWindConfiguration;
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

    private static HInterceptorContext interceptorContext;
    private static final String DEFAULT_NAMESPACE = "/";
    private HWindApplicationContext windApplicationContext;
    private InterceptorFactory interceptorFactory;

    private Map<String, String> nameAndClassNameMap = Maps.newConcurrentMap();
    private Map<String, HWindInterceptor> interceptorMap = Maps.newHashMap();
    private List<HWindInterceptor> interceptors = Lists.newArrayList();


    private HInterceptorContext(HWindApplicationContext windApplicationContext) {
        this.windApplicationContext = windApplicationContext;
        this.interceptorFactory = InterceptorFactory.getInterceptorFactory();
    }

    public static HInterceptorContext getInterceptorContext(HWindApplicationContext windApplicationContext) {
        if (interceptorContext == null) {
            interceptorContext = new HInterceptorContext(windApplicationContext);
        }
        return interceptorContext;
    }

    public void initInterceptorContext(HWindConfiguration configuration) {
        Preconditions.checkNotNull(configuration, "hwind configuration can't be null");

        registerInterceptorName(configuration);
        initInterceptorStack(configuration);
    }


    /**
     * 初始化拦截器配置
     *
     * @param configuration 拦截器的配置信息
     */
    private void registerInterceptorName(HWindConfiguration configuration) {
        registerInterceptorName(configuration.getInterceptorDefines());
    }


    /**
     * 自己用反射实现的简单ioc
     * todo 使用spring的ioc
     *
     * @param interceptors
     */
    private void registerInterceptorName(List<InterceptorDefine> interceptors) {
        if (CollectionUtils.isEmpty(interceptors)) {
            logger.debug("HWind has no interceptors");
            return;
        }
        for (InterceptorDefine interceptorDefine : interceptors) {
            nameAndClassNameMap.put(interceptorDefine.getName(), interceptorDefine.getClassName());
        }
    }

    private void initInterceptorStack(HWindConfiguration configuration) {
        List<InterceptorStack> interceptorStacks = configuration.getInterceptorStacks();
        if (interceptorStacks != null) {
            interceptorStacks.forEach(interceptorStack -> {
                List<InterceptorStack.InterceptorAdapter> interceptorAdapters = interceptorStack.getInterceptorAdapters();
                interceptors = initInterceptors(interceptorAdapters);
            });
        }
    }

    private List<HWindInterceptor> initInterceptors(List<InterceptorStack.InterceptorAdapter> interceptorAdapters) {
        List<HWindInterceptor> interceptors = Lists.newArrayList();
        for (InterceptorStack.InterceptorAdapter interceptorAdapter : interceptorAdapters) {
            List<String> interceptorRefNames = interceptorAdapter.getInterceptorRefNames();
            for (String interceptorName : interceptorRefNames) {
                if (!nameAndClassNameMap.containsKey(interceptorName)) {
                    throw new RuntimeException(String.format("The interceptor which name is [%s] not exists", interceptorName));
                }
                String className = nameAndClassNameMap.get(interceptorName);
                HWindInterceptor interceptor = interceptorFactory.getInterceptor(className, HWindInterceptor.class);
                List<String> mapping = interceptorAdapter.getMapping();
                List<String> excludeMapping = interceptorAdapter.getExcludeMapping();
                MappedInterceptor mappedInterceptor = new MappedInterceptor(mapping.toArray(new String[mapping.size()]), excludeMapping.toArray(new String[excludeMapping.size()]), interceptor);
                interceptors.add(mappedInterceptor);
                interceptorMap.put(interceptorName, mappedInterceptor);
            }
        }
        return interceptors;
    }

    public HWindInterceptor getHWindInterceptor(String namespace, String name) {
        if (Strings.isNullOrEmpty(namespace) || Strings.isNullOrEmpty(name)) {
            logger.error("HWind interceptor's name or namespace can't be null or empty");
            throw new IllegalArgumentException(String.format("illegal argument to get HWind interceptor with name [ %s ] and [ %s ]", name, namespace));
        }
        return interceptorMap.get(name);
    }

    public List<HWindInterceptor> getInterceptors() {
        return interceptors;
    }
}
