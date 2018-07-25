package org.hklrzy.hwind.interceptor;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * Created 2018/6/12.
 * Author ke.hao
 * 拦截器栈定义
 */
public class InterceptorStack {


    List<InterceptorAdapter> interceptorAdapters;


    public static class InterceptorAdapter {
        private List<String> mapping;

        private List<String> excludeMapping;

        private InterceptorDefine interceptorDefine;

        public InterceptorAdapter(List<String> mapping, List<String> excludeMapping, InterceptorDefine interceptorDefine) {
            this.mapping = mapping;
            this.excludeMapping = excludeMapping;
            this.interceptorDefine = interceptorDefine;
        }

        public List<String> getMapping() {
            return mapping;
        }

        public void setMapping(List<String> mapping) {
            this.mapping = mapping;
        }

        public List<String> getExcludeMapping() {
            return excludeMapping;
        }

        public void setExcludeMapping(List<String> excludeMapping) {
            this.excludeMapping = excludeMapping;
        }


        public InterceptorDefine getInterceptorDefine() {
            return interceptorDefine;
        }

        public void setInterceptorDefine(InterceptorDefine interceptorDefine) {
            this.interceptorDefine = interceptorDefine;
        }
    }

    public List<InterceptorAdapter> getInterceptorAdapters() {
        return interceptorAdapters;
    }

    public void setInterceptorAdapters(InterceptorAdapter interceptorAdapter) {
        if (this.interceptorAdapters == null) {
            this.interceptorAdapters = Lists.newArrayList();
        } else {
            interceptorAdapters.add(interceptorAdapter);
        }
    }

    public void setInterceptorAdapters(List<InterceptorAdapter> interceptorAdapters) {
        if (CollectionUtils.isEmpty(this.interceptorAdapters)) {
            this.interceptorAdapters = interceptorAdapters;
        } else {
            this.interceptorAdapters.addAll(interceptorAdapters);
        }
    }

}
