package org.hklrzy.hwind.interceptor;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class HWindInterceptorChain {

    private static Logger logger =
            LoggerFactory.getLogger(HWindInterceptorChain.class);

    private final Object handler;

    private HWindInterceptor[] hWindInterceptors;

    private List<HWindInterceptor> hWindInterceptorList;


    public HWindInterceptorChain(Object handler) {
        this(handler, null);
    }

    public HWindInterceptorChain(Object handler, HWindInterceptor[] hWindInterceptors) {
        this.handler = handler;
        this.hWindInterceptors = hWindInterceptors;
        this.hWindInterceptorList = hWindInterceptorList;
    }

    public boolean applyPreInterceptor(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HWindInterceptor[] hWindInterceptors = getHWindInterceptors();
        if (!ObjectUtils.isEmpty(hWindInterceptors)) {
            for (int i = 0; i < hWindInterceptors.length; i++) {
                HWindInterceptor hWindInterceptor = hWindInterceptors[i];
                if (!hWindInterceptor.preHandler(request, response)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void applyPostInterceptor(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HWindInterceptor[] hWindInterceptors = getHWindInterceptors();
        if (!ObjectUtils.isEmpty(hWindInterceptors)) {
            for (HWindInterceptor hWindInterceptor : hWindInterceptors) {
                hWindInterceptor.postHandler(request, response);
            }
        }
    }

    public HWindInterceptor[] getHWindInterceptors() {
        if (this.hWindInterceptors == null && CollectionUtils.isNotEmpty(hWindInterceptorList)) {
            this.hWindInterceptors = hWindInterceptorList.toArray(new HWindInterceptor[hWindInterceptorList.size()]);
        }
        return this.hWindInterceptors;
    }

    public Object getHandler() {
        return handler;
    }
}
