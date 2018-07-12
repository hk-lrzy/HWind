package org.hklrzy.hwind.mather;

import org.hklrzy.hwind.interceptor.HWindInterceptor;
import org.hklrzy.hwind.interceptor.MappedInterceptor;

/**
 * Author: haoke
 * Created: 2018/7/11
 */
public interface PathMather {

    boolean mather(MappedInterceptor interceptor, String lookup);

}
