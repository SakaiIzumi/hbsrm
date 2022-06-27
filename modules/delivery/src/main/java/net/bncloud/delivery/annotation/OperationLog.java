package net.bncloud.delivery.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OperationLog {
    String content() default "";
    String code() default "";
}
