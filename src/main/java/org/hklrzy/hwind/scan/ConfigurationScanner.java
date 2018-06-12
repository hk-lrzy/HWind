package org.hklrzy.hwind.scan;

import org.apache.commons.collections4.CollectionUtils;
import org.hklrzy.hwind.HWindConfiguration;
import org.hklrzy.hwind.scan.listener.DefaultClassListener;

import java.util.List;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
public class ConfigurationScanner {

    private List<String> basePackages;
    private HWindConfiguration configuration;


    public ConfigurationScanner(HWindConfiguration configuration) {
        this.configuration = configuration;
        this.basePackages = configuration.getBasePackages();
    }

    public void scan() {
        if (CollectionUtils.isNotEmpty(basePackages)) {
            ClassListener classListener = new DefaultClassListener();
            basePackages.forEach(basePackage -> PackageScanner.scan(basePackage, classListener));
        }
    }

}
