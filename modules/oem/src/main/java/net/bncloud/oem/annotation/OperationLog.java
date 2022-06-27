package net.bncloud.oem.annotation;


import java.lang.annotation.*;

/**
 * @author liyh
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OperationLog {
    String content() default "";
    String code() default "";
}
