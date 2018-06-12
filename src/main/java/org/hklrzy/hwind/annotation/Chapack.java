package org.hklrzy.hwind.annotation;

import java.lang.annotation.*;

/**
 * Created 2018/1/11.
 * Author ke.hao
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Chapack {
    String name();

    String namespace();

    String[] interceptors() default {};

}
