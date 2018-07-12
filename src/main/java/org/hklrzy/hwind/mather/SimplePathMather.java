package org.hklrzy.hwind.mather;

import org.hklrzy.hwind.interceptor.MappedInterceptor;

/**
 * Author: haoke
 * Created: 2018/7/11
 */
public class SimplePathMather implements PathMather {
    @Override
    public boolean mather(MappedInterceptor interceptor, String lookup) {

        String[] includePatterns = interceptor.getIncludePatterns();

        boolean includeMatcher = false;
        for (int i = 0; i < includePatterns.length; i++) {
            if (doMatch(includePatterns[i], lookup)) {
                includeMatcher = true;
                break;
            }
        }

        boolean excludeMatcher = false;
        String[] excludePatterns = interceptor.getExcludePatterns();
        for (int i = 0; i < excludePatterns.length; i++) {
            if (doMatch(excludePatterns[i], lookup)) {
                excludeMatcher = true;
                break;
            }
        }
        return !excludeMatcher && includeMatcher;
    }

    //todo
    public boolean doMatch(String pattern, String path) {
        return false;
    }
}
