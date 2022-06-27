package net.bncloud.contract.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OperationLog {
    String content() default "";
    String code() default "";
}
