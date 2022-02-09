package ie.nuig.i3market.semantic.engine.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Chi-Hung Le
 * @email: chihung.le@nuigalway.ie
 * @project i-3-market
 */

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Owner {
    String value() default "";
}
