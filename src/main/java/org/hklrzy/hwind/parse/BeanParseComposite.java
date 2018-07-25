package org.hklrzy.hwind.parse;

import com.google.common.collect.Maps;
import org.hklrzy.hwind.HWindConfiguration;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created 2018/7/25.
 * Author ke.hao
 */
public class BeanParseComposite implements HWindBeanParser {
    private static Logger logger =
            LoggerFactory.getLogger(BeanParseComposite.class);

    private Map<String, HWindBeanParser> nameParserMap = Maps.newConcurrentMap();
    private static BeanParseComposite parseComposite;

    private BeanParseComposite() {
        this.nameParserMap.put("interceptor-def", new InterceptorDefineParser());
        this.nameParserMap.put("interceptor-ref", new InterceptorRefParser());
        this.nameParserMap.put("interceptor-stack", new InterceptorStackParser());
        this.nameParserMap.put("pack", new PackParse());
    }

    public synchronized static BeanParseComposite initBeanParseComposite() {
        if (parseComposite == null) {
            parseComposite = new BeanParseComposite();

        }
        return parseComposite;
    }

    @Override
    public Object parse(Element element, HWindConfiguration configuration) {
        for (Map.Entry<String, HWindBeanParser> parser : nameParserMap.entrySet()) {
            List<Element> elements = element.getChildren(parser.getKey());
            for (Element ele : elements) {
                parser.getValue().parse(ele, configuration);
            }
        }
        return null;
    }

    public Object parse(String location, HWindConfiguration configuration) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(new File(location));
            Element root = document.getRootElement();
            if (root == null) {
                throw new RuntimeException(String.format("parse path [%s] by SAXBuilder failed", location));
            }
            parse(root, configuration);
        } catch (Exception e) {
            logger.error("failed to parse configuration with location file [{}]", location);
        }
        return null;
    }
}