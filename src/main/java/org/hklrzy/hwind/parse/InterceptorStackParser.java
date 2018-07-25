package org.hklrzy.hwind.parse;

import com.google.common.collect.Lists;
import org.hklrzy.hwind.HWindConfiguration;
import org.hklrzy.hwind.interceptor.InterceptorDefine;
import org.hklrzy.hwind.interceptor.InterceptorStack;
import org.hklrzy.hwind.utils.DomUtils;
import org.jdom2.Element;

import java.util.List;


/**
 * Created 2018/7/25.
 * Author ke.hao
 */
public class InterceptorStackParser implements HWindBeanParser {


    @Override
    public Object parse(Element element, HWindConfiguration configuration) {
        List<Element> interceptors = DomUtils.getChildElementsByTagName(element, "interceptor");

        InterceptorStack interceptorStack = new InterceptorStack();

        for (Element interceptor : interceptors) {

            List<String> includePatterns = getIncludePattern(interceptor, "mapping");
            List<String> excludePatterns = getIncludePattern(interceptor, "exclude-mapping");
            Element interceptorDefineElement = getInterceptorDefineOrRef(interceptor, "interceptor-def", "interceptor-ref");
            Object interceptorDefine = configuration.getBeanParseComposite().parse(interceptorDefineElement, configuration);

            if (!(interceptorDefine instanceof InterceptorDefine)) {
                throw new IllegalArgumentException("interceptor stack [{}] has no interceptor-def or interceptor-ref");
            }

            InterceptorStack.InterceptorAdapter interceptorAdapter = new InterceptorStack.InterceptorAdapter(includePatterns, excludePatterns, (InterceptorDefine) interceptorDefine);
            interceptorStack.setInterceptorAdapters(interceptorAdapter);
        }

        configuration.setInterceptorStacks(interceptorStack);
        return null;
    }

    private List<String> getIncludePattern(Element interceptor, String elementName) {
        List<Element> paths = DomUtils.getChildElementsByTagName(interceptor, elementName);
        List<String> patterns = Lists.newArrayList();
        paths.forEach(path -> patterns.add(path.getAttributeValue("path")));
        return patterns;
    }

    public Element getInterceptorDefineOrRef(Element element, String... childElementNames) {
        Element defOrRefElement = null;
        for (String eleName : childElementNames) {
            defOrRefElement = DomUtils.getChildElementByTagName(element, eleName);
            if (defOrRefElement != null) {
                break;
            }
        }
        return defOrRefElement;
    }

}
