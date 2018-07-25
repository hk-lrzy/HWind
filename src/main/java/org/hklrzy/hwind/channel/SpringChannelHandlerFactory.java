package org.hklrzy.hwind.channel;

import com.google.common.base.Strings;
import org.hklrzy.hwind.HWindApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * Created 2018/6/28.
 * Author ke.hao
 */
public class SpringChannelHandlerFactory extends AbstractChannelHandlerFactory {
    private static Logger logger =
            LoggerFactory.getLogger(SpringChannelHandlerFactory.class);

    private ApplicationContext applicationContext;

    public SpringChannelHandlerFactory(HWindApplicationContext applicationContext) {
        this.applicationContext = applicationContext.getParentContext();
    }

    @Override
    public Object getChannelHandler(HWindChannel channel) {
        String beanDefinitionName = Strings.isNullOrEmpty(channel.getName())
                ? getChannelBeanDefinitionName(channel)
                : channel.getName();
        Object handle = null;
        try {
            handle = applicationContext.getBean(beanDefinitionName);
        } catch (NoSuchBeanDefinitionException e) {
            logger.error("can't find bean definition with class name [{}]", channel.getClassName());
        }
        if (handle == null) {
            throw new NoSuchBeanDefinitionException(channel.getClassName());
        }
        return handle;
    }

    private String getChannelBeanDefinitionName(HWindChannel channel) {
        String channelClassName = channel.getClassName();
        String beanDefinitionName = channelClassName;
        if (channelClassName.contains(".")) {
            beanDefinitionName = channelClassName.substring(channelClassName.lastIndexOf(".") + 1);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(beanDefinitionName.substring(0, 1).toLowerCase());
        builder.append(beanDefinitionName.substring(1));
        return builder.toString();
    }
}
