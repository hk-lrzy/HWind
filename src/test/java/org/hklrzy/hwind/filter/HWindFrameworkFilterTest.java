package org.hklrzy.hwind.filter;

import org.junit.Test;

import javax.servlet.ServletException;

/**
 * Created 2018/6/12.
 * Author ke.hao
 */
public class HWindFrameworkFilterTest {

    @Test
    public void init() throws ServletException {
        HWindFrameworkFilter hWindFrameworkFilter = new HWindFrameworkFilter();
        hWindFrameworkFilter.init(null);

    }

    @Test
    public void doFilter() {
    }

    @Test
    public void destroy() {
    }
}