package org.hklrzy.hwind.filter;

import org.hklrzy.hwind.HWindApplicationContext;
import org.hklrzy.hwind.HWindConfiguration;
import org.junit.Test;

import javax.servlet.ServletException;

/**
 * Created 2018/6/12.
 * Author ke.hao
 */
public class HWindFrameworkFilterTest {

    @Test
    public void init() throws Exception {
        HWindConfiguration configuration = HWindConfiguration.newClassPathConfiguration("hwind.xml");
        HWindApplicationContext applicationContext = HWindApplicationContext.getApplicationContext();
        applicationContext.init(configuration,null);
    }

    @Test
    public void doFilter() {
    }

    @Test
    public void destroy() {
    }
}