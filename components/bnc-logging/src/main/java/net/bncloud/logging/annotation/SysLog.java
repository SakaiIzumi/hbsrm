package net.bncloud.logging.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SysLog {

    /**
     * 模块名
     */
    String module() default "";

    /**
     * 操作
     */
    String action() default "";

    /**
     * 资源描述
     */
    String resource() default "";

    /**
     * 为 false 时不记录日志
     */
    boolean enable() default true;

    /**
     * 为 true 时不记录返回值
     */
    boolean ignoreResponse() default false;
}
