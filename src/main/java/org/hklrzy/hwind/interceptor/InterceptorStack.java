package org.hklrzy.hwind.interceptor;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created 2018/6/12.
 * Author ke.hao
 */
public class InterceptorStack {

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
}
