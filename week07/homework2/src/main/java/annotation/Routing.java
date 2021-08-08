package annotation;

import config.enums.Datasources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Created by wwh
 * @date 2021/8/8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Routing {
    String value() default Datasources.MASTER;
}
