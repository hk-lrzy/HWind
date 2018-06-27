package org.hklrzy.hwind.scan;

import org.hklrzy.hwind.exception.HWindScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * Created 2018/6/12.
 * Author ke.hao
 */
public class PackageScanner {
    private static Logger logger =
            LoggerFactory.getLogger(PackageScanner.class);


    @SuppressWarnings("all")
    public static void scan(String packageName, ClassListener classListener) {
        String packagePath = packageName.replaceAll("\\.", "/");
        URL url = PackageScanner.class.getClassLoader().getResource(packagePath);
        try {
            scan(packageName, url.toURI(), classListener);
        } catch (Exception e) {
            logger.error("scan package name [ {} ] failed", packageName);
            throw new HWindScanException();
        }
    }

    public static void scan(String packageName, URI uri, ClassListener classListener) {
        File basePackageFile = new File(packageName);
        if (!basePackageFile.exists()) {
            logger.debug("scan from package name [ {} ] is not exists", packageName);
            return;
        }
        if (basePackageFile.isDirectory()) {
            File[] childrenFiles = basePackageFile.listFiles();
            for (File childrenFile : childrenFiles) {
                scan(packageName, childrenFile.toURI(), classListener);
            }
        } else if (basePackageFile.getName().endsWith(".class")) {
            scanClass(packageName, basePackageFile, classListener);
        }
    }

    private static void scanClass(String packageName, File file, ClassListener listener) {
        String filePath = file.getPath().replaceAll("/", ".").replaceAll("\\\\", ".");
        String className = filePath.substring(filePath.indexOf(packageName), filePath.lastIndexOf("."));
        try {
            Class<?> clazz = Class.forName(className);
            if (listener != null) {
                listener.listen(clazz);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
