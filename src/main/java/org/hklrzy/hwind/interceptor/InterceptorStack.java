package org.hklrzy.hwind.interceptor;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created 2018/6/12.
 * Author ke.hao
 * 拦截器栈定义
 */
public class InterceptorStack {

    private List<String> namespaces;

    private List<String> excludeNameSpaces;

    private String name;

    private List<String> interceptorRefNames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getInterceptorRefNames() {
        return CollectionUtils.isEmpty(interceptorRefNames) ? Collections.EMPTY_LIST : interceptorRefNames;
    }

    public void setInterceptorRefNames(List<String> interceptorRefNames) {
        this.interceptorRefNames = interceptorRefNames;
    }

    public List<String> getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(List<String> namespaces) {
        this.namespaces = namespaces;
    }

    public List<String> getExcludeNameSpaces() {
        return excludeNameSpaces;
    }

    public void setExcludeNameSpaces(List<String> excludeNameSpaces) {
        this.excludeNameSpaces = excludeNameSpaces;
    }
}
