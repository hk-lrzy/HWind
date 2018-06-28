package org.hklrzy.hwind.channel;

import org.hklrzy.hwind.HWindApplicationContext;
import org.hklrzy.hwind.HWindChannel;
import org.springframework.context.ApplicationContext;

/**
 * Created 2018/6/28.
 * Author ke.hao
 */
public class SpringChannelHandlerFactory extends AbstractChannelHandlerFactory {

    private ApplicationContext applicationContext;

    public SpringChannelHandlerFactory(HWindApplicationContext applicationContext) {
        this.applicationContext = applicationContext.getWebApplicationContext();
    }

    @Override
    public Object getChannelHandler(HWindChannel channel) {
        return applicationContext.getBean(channel.getClassName());
    }
}
