package org.hklrzy.hwind.parse;

import org.hklrzy.hwind.HWindConfiguration;
import org.hklrzy.hwind.constants.HWindConstants;
import org.hklrzy.hwind.interceptor.InterceptorDefine;
import org.jdom2.Element;

/**
 * Created 2018/7/25.
 * Author ke.hao
 */
public class InterceptorDefineParser implements HWindBeanParser {
    @Override
    public Object parse(Element element, HWindConfiguration configuration) {

        InterceptorDefine interceptorDefine = new InterceptorDefine();

        interceptorDefine.setClassName(element.getAttributeValue(HWindConstants.HWIND_CONFIG_CLASS));
        interceptorDefine.setName(element.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME));

        configuration.setInterceptorDefine(interceptorDefine);

        return interceptorDefine;
    }
}
