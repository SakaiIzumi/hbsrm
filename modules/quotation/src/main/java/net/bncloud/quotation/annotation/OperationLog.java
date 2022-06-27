package net.bncloud.quotation.annotation;

import java.lang.annotation.*;

/**
 * @author Toby
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OperationLog {
    String content() default "";
    String code() default "";
}
