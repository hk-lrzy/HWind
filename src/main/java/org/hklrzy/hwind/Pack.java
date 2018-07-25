package org.hklrzy.hwind;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.hklrzy.hwind.channel.HWindChannel;

import java.util.List;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class Pack {

    private String name;
    private String namespace;
    private List<HWindChannel> channels;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public List<HWindChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<HWindChannel> channels) {
        if (CollectionUtils.isEmpty(this.channels)) {
            this.channels = channels;
        } else {
            this.channels.addAll(channels);
        }
    }

    public void setChannel(HWindChannel channel) {
        setChannels(Lists.newArrayList(channel));
    }

}
