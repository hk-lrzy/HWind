package org.hklrzy.hwind.channel;

import com.google.common.collect.Maps;
import org.hklrzy.hwind.HWindApplicationContext;
import org.hklrzy.hwind.HWindConfiguration;
import org.hklrzy.hwind.Pack;
import org.hklrzy.hwind.interceptor.HInterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class HChannelContext {
    private static Logger logger =
            LoggerFactory.getLogger(org.hklrzy.hwind.channel.HChannelContext.class);

    /**
     * channel 上下文信息
     */
    private static HChannelContext channelContext;

    /**
     * 拦截器配置
     */
    private HInterceptorContext interceptorContext;

    /**
     * uri与相应channel的映射
     */
    private Map<String, HWindChannel> channels = Maps.newHashMap();

    private AbstractChannelHandlerFactory channelHandlerFactory;

    private static final Object lock = new Object();

    private HChannelContext() {
    }

    public static HChannelContext getHChannelContext() {
        if (channelContext == null) {
            synchronized (lock) {
                if (channelContext == null) {
                    channelContext = new HChannelContext();
                }
            }
        }
        return channelContext;
    }

    public void init(HWindApplicationContext applicationContext) {
        if (applicationContext == null) {
            logger.error("HWind init HChannelContext failed because configuration is null");
            throw new IllegalArgumentException("HWind init HChannelContext failed because configuration is null");
        }
        channelHandlerFactory = AbstractChannelHandlerFactory.getFactory(applicationContext);
        initInterceptorContext(applicationContext);
        registerChannels(applicationContext);

    }

    private void initInterceptorContext(HWindApplicationContext applicationContext) {

    }

    private void registerChannels(HWindApplicationContext applicationContext) {
        HWindConfiguration configuration = applicationContext.getConfiguration();

        List<Pack> packs = configuration.getPacks();

        if (packs == null) {
            return;
        }

        for (Pack pack : packs) {
            for (HWindChannel channel : pack.getChannels()) {

                channel.setPack(pack);

                channels.put(channel.getCanonicalName(), channel);
                logger.info("HWind register path [ {} ]", channel.getCanonicalName());

                initChannelHandler(channel);
            }
        }
    }

    public HWindChannel getDefaultChannel() {
        return null;
    }


    private void initChannelHandler(HWindChannel channel) {
        channel.setChannelHandler(channelHandlerFactory.getChannelHandler(channel));
    }

    public HWindChannel getChannel(String uri) {
        return channels.get(uri);
    }
}
