package org.hklrzy.hwind;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.hklrzy.hwind.interceptor.InterceptorDefine;
import org.hklrzy.hwind.interceptor.InterceptorStack;
import org.hklrzy.hwind.parse.BeanParseComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class HWindConfiguration {
    private static Logger logger =
            LoggerFactory.getLogger(HWindConfiguration.class);

    private static final String DEFAULT_CONFIG_FILE_NAME = "hwind.xml";
    private static final String DEFAULT_CONFIG_NAME = "config";

    private String location;
    private Configuration configuration;
    private BeanParseComposite beanParseComposite;


    private HWindConfiguration(String path) throws IOException {
        this.location = path;
        this.configuration = new Configuration();
        this.beanParseComposite = BeanParseComposite.initBeanParseComposite();
        loadAllConfiguration();
    }

    /**
     * 获取所有的配置文件
     *
     * @throws Exception
     */
    private void loadAllConfiguration() throws IOException {

        //1. 解析xml文件配置
        loadConfiguration();

        //2. 解析注解配置
        //loadAnnotationConfiguration();
    }

    private void loadConfiguration() {
        beanParseComposite.parse(location, this);
    }

    private void loadAnnotationConfiguration() {

    }


    public static HWindConfiguration newClassPathConfiguration(ServletConfig servletConfig) {
        try {
            return new HWindConfiguration(getParamPath(servletConfig));
        } catch (Exception e) {
            logger.error("get instance configuration failed ", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static String getConfig(ServletConfig servletConfig) {
        return Strings.isNullOrEmpty(servletConfig.getInitParameter(DEFAULT_CONFIG_NAME))
                ? DEFAULT_CONFIG_FILE_NAME
                : servletConfig.getInitParameter(DEFAULT_CONFIG_NAME);
    }

    private static String getParamPath(ServletConfig servletConfig) {
        return HWindConfiguration.class.getClassLoader().getResource(getConfig(servletConfig)).getPath();
    }


    public List<InterceptorDefine> getInterceptorDefines() {
        return configuration.getInterceptorDefines();
    }

    public InterceptorDefine getInterceptorDefine(String name) {
        return configuration.getInterceptorDefineMap().get(name);
    }

    public List<Pack> getPacks() {
        return configuration.packs;
    }

    public List<String> getBasePackages() {
        return configuration.basePackages;
    }

    public List<InterceptorStack> getInterceptorStacks() {
        return configuration.interceptorStacks;
    }

    public BeanParseComposite getBeanParseComposite() {
        return beanParseComposite;
    }

    public Configuration getConfiguration() {
        return configuration;
    }


    public void setInterceptorStacks(InterceptorStack interceptorStack) {
        configuration.setInterceptorStacks(interceptorStack);
    }

    public void setInterceptorDefine(InterceptorDefine interceptorDefine) {
        configuration.setInterceptorDefines(interceptorDefine);
    }

    public void setPack(Pack pack) {
        configuration.setPacks(Lists.newArrayList(pack));
    }

    protected class Configuration {
        private String path;
        private List<Pack> packs;
        private Map<String, InterceptorDefine> interceptorDefineMap;
        private List<InterceptorDefine> interceptorDefines;
        private List<InterceptorStack> interceptorStacks;
        private List<String> basePackages;


        private Configuration() {
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public List<Pack> getPacks() {
            return packs;
        }

        public void setPacks(List<Pack> packs) {
            if (CollectionUtils.isEmpty(this.packs)) {
                this.packs = packs;
            } else {
                this.packs.addAll(packs);
            }
        }

        public List<InterceptorDefine> getInterceptorDefines() {
            return interceptorDefines;
        }

        public void setInterceptorDefines(InterceptorDefine interceptorDefine) {
            if (CollectionUtils.isEmpty(interceptorDefines)) {
                interceptorDefines = Lists.newArrayList();
                interceptorDefineMap = Maps.newHashMap();
            }
            interceptorDefineMap.put(interceptorDefine.getName(), interceptorDefine);
            interceptorDefines.add(interceptorDefine);
        }

        public List<InterceptorStack> getInterceptorStacks() {
            return interceptorStacks;
        }

        public void setInterceptorStacks(InterceptorStack interceptorStack) {
            if (CollectionUtils.isEmpty(interceptorStacks)) {
                interceptorStacks = Lists.newArrayList();
            }
            interceptorStacks.add(interceptorStack);
        }

        public List<String> getBasePackages() {
            return basePackages;
        }

        public void setBasePackages(List<String> basePackages) {
            this.basePackages = basePackages;
        }

        public Map<String, InterceptorDefine> getInterceptorDefineMap() {
            return interceptorDefineMap;
        }
    }
}
