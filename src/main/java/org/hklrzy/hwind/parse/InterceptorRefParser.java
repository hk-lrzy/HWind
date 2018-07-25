package org.hklrzy.hwind.parse;

import org.hklrzy.hwind.HWindConfiguration;
import org.hklrzy.hwind.constants.HWindConstants;
import org.hklrzy.hwind.interceptor.InterceptorDefine;
import org.jdom2.Element;

/**
 * Created 2018/7/25.
 * Author ke.hao
 */
public class InterceptorRefParser implements HWindBeanParser {

    @Override
    public Object parse(Element element, HWindConfiguration configuration) {


        String interceptorName = element.getAttributeValue(HWindConstants.HWIND_CONFIG_INTERCEPTOR_REF);

        InterceptorDefine interceptorDefine = configuration.getInterceptorDefine(interceptorName);

        if (interceptorDefine == null) {
            throw new IllegalArgumentException(String.format("interceptor [%s] didn't register but reference it", interceptorName));
        }

        return interceptorDefine;
    }
}
