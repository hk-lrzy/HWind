package org.hklrzy.hwind.interceptor;

import org.hklrzy.hwind.annotation.Interceptor;

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

        private List<InterceptorDefine> interceptorDefNames;

        private List<String> interceptorRefNames;

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


        public List<InterceptorDefine> getInterceptorDefNames() {
            return interceptorDefNames;
        }

        public void setInterceptorDefNames(List<InterceptorDefine> interceptorDefNames) {
            this.interceptorDefNames = interceptorDefNames;
        }

        public List<String> getInterceptorRefNames() {
            return interceptorRefNames;
        }

        public void setInterceptorRefNames(List<String> interceptorRefNames) {
            this.interceptorRefNames = interceptorRefNames;
        }
    }

    public List<InterceptorAdapter> getInterceptorAdapters() {
        return interceptorAdapters;
    }

    public void setInterceptorAdapters(List<InterceptorAdapter> interceptorAdapters) {
        this.interceptorAdapters = interceptorAdapters;
    }

}
