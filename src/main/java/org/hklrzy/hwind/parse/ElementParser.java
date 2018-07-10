package org.hklrzy.hwind.parse;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.hklrzy.hwind.HWindChannel;
import org.hklrzy.hwind.Pack;
import org.hklrzy.hwind.constants.HWindConstants;
import org.hklrzy.hwind.interceptor.InterceptorDefine;
import org.hklrzy.hwind.interceptor.InterceptorStack;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created 2018/6/12.
 * Author ke.hao
 */
public class ElementParser {
    private static Logger logger =
            LoggerFactory.getLogger(ElementParser.class);


    private static ElementParser parser;

    public static ElementParser instance() {
        if (parser == null) {
            parser = new ElementParser();
        }
        return parser;
    }

    private ElementParser() {
    }


    /**
     * 解析拦截器的定义
     *
     * @param elements
     * @return
     */
    public List<InterceptorDefine> parseInterceptorDefines(List<Element> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            if (logger.isDebugEnabled()) {
                logger.debug("hwind elements can't find element [interceptor-def]");
            }
            return null;
        }
        List<InterceptorDefine> interceptorDefines = Lists.newArrayList();
        elements.forEach(element -> {
            InterceptorDefine interceptorDefine = new InterceptorDefine();
            interceptorDefine.setName(element.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME));
            interceptorDefine.setClassName(element.getAttributeValue(HWindConstants.HWIND_CONFIG_CLASS));
            interceptorDefines.add(interceptorDefine);
        });

        return interceptorDefines;
    }

    public List<String> parseInterceptorNames(List<Element> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        return elements.stream().map(element -> element.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME)).collect(Collectors.toList());
    }

    public List<InterceptorStack> parseInterceptorStacks(List<Element> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        return elements.stream().map(element -> {
            InterceptorStack interceptorStack = new InterceptorStack();
            interceptorStack.setInterceptorAdapters(parseInterceptor(element.getChildren(HWindConstants.HWIND_CONFIG_INTERCEPTOR)));
            return interceptorStack;
        }).collect(Collectors.toList());
    }

    public List<InterceptorStack.InterceptorAdapter> parseInterceptor(List<Element> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        return elements.stream().map(element -> {
            List<Element> mappingElements = element.getChildren(HWindConstants.HWIND_CONFIG_INTERCEPTOR_MAPPING);
            List<Element> excludeMappingElements = element.getChildren(HWindConstants.HWIND_CONFIG_INTERCEPTOR_EXCLUDE_MAPPING);
            List<String> mappingValues = mappingElements.stream().map(mappingElement -> mappingElement.getAttributeValue(HWindConstants.HWIND_CONFIG_INTERCEPTOR_PATH)).collect(Collectors.toList());
            List<String> excludeMappingValues = excludeMappingElements.stream().map(mappingElement -> mappingElement.getAttributeValue(HWindConstants.HWIND_CONFIG_INTERCEPTOR_PATH)).collect(Collectors.toList());
            List<String> interceptorRefNames = element.getChildren(HWindConstants.HWIND_CONFIG_INTERCEPTOR_REFERENCE).stream().map(mappingElement -> mappingElement.getAttributeValue(HWindConstants.HWIND_CONFIG_INTERCEPTOR_REF)).collect(Collectors.toList());
            List<InterceptorDefine> interceptorDefines = this.parseInterceptorDefines(element.getChildren(HWindConstants.HWIND_CONFIG_INTERCEPTOR_DEFINE));
            InterceptorStack.InterceptorAdapter interceptorAdapter = new InterceptorStack.InterceptorAdapter();
            interceptorAdapter.setExcludeMapping(excludeMappingValues);
            interceptorAdapter.setMapping(mappingValues);
            interceptorAdapter.setInterceptorRefNames(interceptorRefNames);
            interceptorAdapter.setInterceptorDefNames(interceptorDefines);
            return interceptorAdapter;
        }).collect(Collectors.toList());
    }


    public List<HWindChannel> parseChannels(List<Element> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        return elements.stream().map(element -> {
            HWindChannel channel = new HWindChannel();
            channel.setName(element.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME));
            channel.setMethodName(element.getAttributeValue(HWindConstants.HWIND_CONFIG_METHOD));
            channel.setClassName(element.getAttributeValue(HWindConstants.HWIND_CONFIG_CLASS));
            channel.setInterceptorRefNames(parseInterceptorNames(element.getChildren(HWindConstants.HWIND_CONFIG_INTERCEPTOR)));
            parseParams(element.getChildren(HWindConstants.HWDIN_CONFIG_PARAM), channel);
            return channel;
        }).collect(Collectors.toList());
    }

    public void parseParams(List<Element> elements, HWindChannel channel) {
        if (CollectionUtils.isNotEmpty(elements)) {

            List<String> paramNames = Lists.newArrayList();
            List<Class<?>> paramTypes = Lists.newArrayList();
            for (Element element : elements) {
                String name = element.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME);
                paramNames.add(name);
                String className = element.getAttributeValue(HWindConstants.HWIND_CONFIG_CLASS);
                try {
                    paramTypes.add(Class.forName(className));
                } catch (Exception e) {
                    logger.error("class [ {} ] not found, please check channel's [ {} ] config", className, channel.getName(), e);
                    throw new RuntimeException(e.getMessage());
                }
            }
            channel.setParameterNames(paramNames);
            channel.setParameterTypes(paramTypes);
        }

    }

    public List<String> parseBasePackages(List<Element> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        return elements.stream().map(element -> element.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME)).collect(Collectors.toList());
    }

    public List<Pack> parsePacks(List<Element> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        return elements.parallelStream().map(element -> {
            Pack pack = new Pack();
            pack.setName(element.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME));
            pack.setNamespace(element.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME_SPACE));
            Element defaultClass = element.getChild(HWindConstants.HWIND_CONFIG_DEFAULT_CLASS);

            if (defaultClass != null) {
                pack.setDefaultClassName(defaultClass.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME));
            }
            pack.setInterceptorDefines(parseInterceptorDefines(element.getChildren(HWindConstants.HWIND_CONFIG_INTERCEPTOR_DEFINE)));
            pack.setInterceptorStacks(parseInterceptorStacks(element.getChildren(HWindConstants.HWIND_CONFIG_INTERCEPTOR_STACK)));
            pack.setInterceptorRefName(parseInterceptorNames(element.getChildren(HWindConstants.HWIND_CONFIG_INTERCEPTOR)));

            pack.setChannels(parseChannels(element.getChildren(HWindConstants.HWIND_CONFIG_CHANNEL)));
            return pack;
        }).collect(Collectors.toList());
    }

}
