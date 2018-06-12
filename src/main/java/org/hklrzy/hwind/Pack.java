package org.hklrzy.hwind;

import org.hklrzy.hwind.interceptor.InterceptorDefine;

import java.util.List;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class Pack {

    private String name;
    private String namespace;

    private List<InterceptorDefine> interceptorDefines;
    private String defaultClassName;

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

    public List<InterceptorDefine> getInterceptorDefines() {
        return interceptorDefines;
    }

    public void setInterceptorDefines(List<InterceptorDefine> interceptorDefines) {
        this.interceptorDefines = interceptorDefines;
    }

    public String getDefaultClassName() {
        return defaultClassName;
    }

    public void setDefaultClassName(String defaultClassName) {
        this.defaultClassName = defaultClassName;
    }

    public List<HWindChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<HWindChannel> channels) {
        this.channels = channels;
    }
}
