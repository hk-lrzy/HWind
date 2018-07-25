package org.hklrzy.hwind.parse;

import org.apache.commons.collections4.CollectionUtils;
import org.hklrzy.hwind.HWindConfiguration;
import org.hklrzy.hwind.Pack;
import org.hklrzy.hwind.channel.HWindChannel;
import org.hklrzy.hwind.constants.HWindConstants;
import org.hklrzy.hwind.utils.DomUtils;
import org.hklrzy.hwind.utils.TypeUtils;
import org.jdom2.Element;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created 2018/7/25.
 * Author ke.hao
 */
public class PackParse implements HWindBeanParser {


    @Override
    public Object parse(Element element, HWindConfiguration configuration) {

        Pack pack = new Pack();

        pack.setNamespace(element.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME_SPACE));
        pack.setName(element.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME));

        List<Element> channels = DomUtils.getChildElementsByTagName(element, "channel");

        for (Element channel : channels) {

            HWindChannel windChannel = getChannel(channel);

            pack.setChannel(windChannel);
        }

        configuration.setPack(pack);
        return null;
    }


    private HWindChannel getChannel(Element element) {
        HWindChannel channel = new HWindChannel();
        channel.setNamespace(element.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME_SPACE));
        channel.setName(element.getAttributeValue(HWindConstants.HWIND_CONFIG_NAME));
        channel.setClassName(element.getAttributeValue(HWindConstants.HWIND_CONFIG_CLASS));
        channel.setMethodName(element.getAttributeValue(HWindConstants.HWIND_CONFIG_METHOD));

        List<Element> params = DomUtils.getChildElementsByTagName(element, "param");

        List<String> nameParamAttribute = getParamAttribute(params, HWindConstants.HWIND_CONFIG_NAME);
        List<String> typeParamAttribute = getParamAttribute(params, HWindConstants.HWIND_CONFIG_TYPE);

        channel.setParameterNames(TypeUtils.toStringArray(nameParamAttribute));
        channel.setParameterTypeNames(typeParamAttribute);

        return channel;
    }

    private List<String> getParamAttribute(List<Element> params, String attributeName) {
        if (CollectionUtils.isNotEmpty(params)) {
            return params.stream().map(param -> param.getAttributeValue(attributeName)).collect(Collectors.toList());
        }
        return null;
    }


}
