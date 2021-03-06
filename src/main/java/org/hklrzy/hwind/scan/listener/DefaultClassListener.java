package org.hklrzy.hwind.scan.listener;

import org.hklrzy.hwind.Pack;
import org.hklrzy.hwind.annotation.Chapack;
import org.hklrzy.hwind.scan.ClassListener;

/**
 * Created 2018/6/12.
 * Author ke.hao
 */
public class DefaultClassListener implements ClassListener {

    private static final Class<Chapack> DEFAULT_ANNOTATION = Chapack.class;

    @Override
    public void listen(Class<?> clazz) {
        if (clazz.getAnnotation(Chapack.class) != null) {
            loadPack(clazz);
        }
    }

    private void loadPack(Class<?> clazz) {
        Chapack annotation = clazz.getAnnotation(DEFAULT_ANNOTATION);
        Pack pack = new Pack();
    }


}
