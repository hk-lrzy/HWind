package org.hklrzy.hwind.channel;

import com.google.common.collect.Maps;
import org.hklrzy.hwind.HWindChannel;
import org.hklrzy.hwind.HWindConfiguration;
import org.hklrzy.hwind.Pack;
import org.hklrzy.hwind.interceptor.InterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class ChannelContext {
    private static Logger logger =
            LoggerFactory.getLogger(ChannelContext.class);

    /**
     * channel 上下文信息
     */
    private static ChannelContext channelContext;

    /**
     * 拦截器配置
     */
    private InterceptorContext interceptorContext;

    /**
     * uri与相应channel的映射
     */
    private Map<String, HWindChannel> channels = Maps.newHashMap();

    private static final Object lock = new Object();

    private ChannelContext() {
    }

    public static ChannelContext instance() {
        if (channelContext == null) {
            synchronized (lock) {
                if (channelContext == null) {
                    channelContext = new ChannelContext();
                }
            }
        }
        return channelContext;
    }

    public void init(HWindConfiguration configuration) {
        if (configuration == null) {
            logger.error("HWind init channelContext failed because configuration is null");
            throw new IllegalArgumentException("HWind init channelContext failed because configuration is null");
        }
        for (Pack pack : configuration.getPacks()) {
            for (HWindChannel channel : pack.getChannels()) {
                channel.setPack(pack);
                channels.put(channel.getCanonicalName(), channel);
            }
        }
    }

    public HWindChannel getChannel(String uri) {
        return channels.get(uri);
    }
}
