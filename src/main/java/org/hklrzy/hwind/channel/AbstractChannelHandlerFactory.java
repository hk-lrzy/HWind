package org.hklrzy.hwind.channel;

import org.hklrzy.hwind.HWindApplicationContext;
import org.hklrzy.hwind.HWindChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created 2018/6/28.
 * Author ke.hao
 */
public abstract class AbstractChannelHandlerFactory {
    private static Logger logger =
            LoggerFactory.getLogger(AbstractChannelHandlerFactory.class);

    private static AbstractChannelHandlerFactory factory;

    AbstractChannelHandlerFactory() {
    }

    public static AbstractChannelHandlerFactory getFactory(HWindApplicationContext applicationContext) {
        if (factory == null) {
            if (applicationContext == null) {
                logger.debug("HWind root application context is empty ,use custom factory bean");
                factory = null; //todo
            } else {
                factory = new SpringChannelHandlerFactory(applicationContext);
            }
        }
        return factory;
    }

    public abstract Object getChannelHandler(HWindChannel channel);
}
