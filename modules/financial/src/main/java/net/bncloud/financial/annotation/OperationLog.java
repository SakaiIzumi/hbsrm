package net.bncloud.financial.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author ddh
 * @version 1.0.0
 * @date 2021/12/20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OperationLog {

    String content() default "";

    String code() default "";

    String billType() default "";
}
