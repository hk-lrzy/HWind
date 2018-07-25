package org.hklrzy.hwind.parse;

import org.hklrzy.hwind.HWindConfiguration;
import org.jdom2.Element;


/**
 * Created 2018/7/25.
 * Author ke.hao
 */
public interface HWindBeanParser {

    Object parse(Element element, HWindConfiguration configuration);
}
