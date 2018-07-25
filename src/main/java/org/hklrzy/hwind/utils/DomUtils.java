package org.hklrzy.hwind.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jdom2.Element;

import java.util.Collection;
import java.util.List;

/**
 * Created 2018/7/25.
 * Author ke.hao
 */
public class DomUtils {

    public static List<Element> getChildElementsByTagName(Element element, String... childEleNames) {
        Preconditions.checkNotNull(element, "Element must not be null");
        Preconditions.checkNotNull(childEleNames, "Element names collection must not be null");
        List<String> childEleNameList = Lists.newArrayList(childEleNames);
        List<Element> elementList = element.getChildren();
        List<Element> childEles = Lists.newArrayList();
        for (int i = 0; i < elementList.size(); i++) {
            if (eleNameMatch(elementList.get(i), childEleNameList)) {
                childEles.add(elementList.get(i));
            }
        }
        return childEles;
    }

    public static Element getChildElementByTagName(Element element, String childEleName) {
        Preconditions.checkNotNull(element, "Element must not be null");
        Preconditions.checkNotNull(childEleName, "Element name collection must not be null");
        List<Element> elementList = element.getChildren();
        for (int i = 0; i < elementList.size(); i++) {
            if (eleNameMatch(elementList.get(i), childEleName)) {
                return elementList.get(i);
            }
        }
        return null;
    }

    private static boolean eleNameMatch(Element element, Collection<?> desiredNames) {
        return (desiredNames.contains(element.getName()));
    }

    private static boolean eleNameMatch(Element element, String desiredName) {
        return (desiredName.equals(element.getName()));
    }
}
