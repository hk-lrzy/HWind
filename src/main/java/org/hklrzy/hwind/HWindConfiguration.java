package org.hklrzy.hwind;

import com.google.common.base.Strings;
import org.hklrzy.hwind.interceptor.InterceptorDefine;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
    private List<InterceptorDefine> interceptorDefines;
    private List<Pack> packs;

    public HWindConfiguration(String path) throws Exception {
        this.path = path;
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(new File(path));
        Element root = document.getRootElement();
        if (root == null) {
            throw new RuntimeException("read config file error");

        }

    }

    public static HWindConfiguration newClassPathConfiguration(String config) {
        config = Strings.isNullOrEmpty(config) ? DEFAULT_CONFIG_NAME : config;
        try {
            return new HWindConfiguration(HWindConfiguration.class.getClassLoader().getResource(config).getPath());
        } catch (Exception e) {
            logger.error("HWind configuration file [ {} ] not founded", config, e);
        }
        return null;
    }


    public List<InterceptorDefine> getInterceptorDefines() {
        return interceptorDefines;
    }

    public List<Pack> getPacks() {
        return packs;
    }
}
