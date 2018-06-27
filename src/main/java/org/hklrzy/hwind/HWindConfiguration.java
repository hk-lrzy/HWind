package org.hklrzy.hwind;

import com.google.common.base.Strings;
import org.apache.commons.collections4.CollectionUtils;
import org.hklrzy.hwind.constants.HWindConstants;
import org.hklrzy.hwind.interceptor.InterceptorDefine;
import org.hklrzy.hwind.interceptor.InterceptorStack;
import org.hklrzy.hwind.parse.ElementParser;
import org.hklrzy.hwind.scan.ConfigurationScanner;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;


/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class HWindConfiguration {
    private static Logger logger =
            LoggerFactory.getLogger(HWindConfiguration.class);

    private static final String DEFAULT_CONFIG_NAME = "hwind.xml";

    private String path;
    private List<Pack> packs;
    private List<InterceptorDefine> interceptorDefines;
    private List<InterceptorStack> interceptorStacks;
    private List<String> basePackages;

    public HWindConfiguration(String path) throws Exception {
        this.path = path;
        parseConfiguration();
    }

    /**
     * 解析配置文件以及扫描目录信息
     *
     * @throws Exception
     */
    private void parseConfiguration() throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(new File(path));
        Element root = document.getRootElement();
        if (root == null) {
            throw new RuntimeException(String.format("HWind parse path [ %s ] with SAXBuilder failed", this.path));
        }
        ElementParser elementParser = ElementParser.instance();
        this.interceptorDefines = elementParser.parseInterceptorDefines(root.getChildren(HWindConstants.HWIND_CONFIG_INTERCEPTOR_DEFINE));
        this.interceptorStacks = elementParser.parseInterceptorStacks(root.getChildren(HWindConstants.HWIND_CONFIG_INTERCEPTOR_STACK));
        this.basePackages = elementParser.parseBasePackages(root.getChildren(HWindConstants.HWINW_CONFIG_SCAN));
        this.packs = elementParser.parsePacks(root.getChildren(HWindConstants.HWIND_CONFIG_PACK));
        if (CollectionUtils.isNotEmpty(basePackages)) {
            ConfigurationScanner configurationScanner = new ConfigurationScanner(this);
            configurationScanner.scan();
        }
    }


    public static HWindConfiguration newClassPathConfiguration(String config) {
        config = Strings.isNullOrEmpty(config) ? DEFAULT_CONFIG_NAME : config;
        try {
            ClassLoader classLoader = HWindConfiguration.class.getClassLoader();
            URL resource = classLoader.getResource(config);
            String path = HWindConfiguration.class.getClassLoader().getResource(config).getPath();
            return new HWindConfiguration(path);
        } catch (Exception e) {
            logger.error("HWind configuration file [ {} ] not founded", config, e);
            throw new IllegalArgumentException(e);
        }
    }


    public List<InterceptorDefine> getInterceptorDefines() {
        return interceptorDefines;
    }

    public List<Pack> getPacks() {
        return packs;
    }

    public List<String> getBasePackages() {
        return basePackages;
    }

}
