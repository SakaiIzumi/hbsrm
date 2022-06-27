package net.bncloud.security.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@FeignClient
public @interface AuthorizedFeignClient {

    @AliasFor(annotation = FeignClient.class, attribute = "name")
    String name() default "";

    @AliasFor(annotation = FeignClient.class, attribute = "contextId")
    String contextId() default "";
    /**
     * A custom {@code @Configuration} for the feign client.
     *
     * Can contain override {@code @Bean} definition for the pieces that
     * make up the client, for instance {@link feign.codec.Decoder},
     * {@link feign.codec.Encoder}, {@link feign.Contract}.
     *
     * @return the custom {@code @Configuration} for the feign client.
     * @see FeignClientsConfiguration for the defaults.
     */
    @AliasFor(annotation = FeignClient.class, attribute = "configuration")
    Class<?>[] configuration() default OAuth2InterceptedFeignConfiguration.class;

    /**
     * An absolute URL or resolvable hostname (the protocol is optional).
     * @return the URL.
     */
    @AliasFor(annotation = FeignClient.class, attribute = "url")
    String url() default "";

    /**
     * Whether 404s should be decoded instead of throwing FeignExceptions.
     * @return true if 404s will be decoded; false otherwise.
     */
    @AliasFor(annotation = FeignClient.class, attribute = "decode404")
    boolean decode404() default false;

    /**
     * Fallback class for the specified Feign client interface. The fallback class must
     * implement the interface annotated by this annotation and be a valid Spring bean.
     * @return the fallback class for the specified Feign client interface.
     */
    @AliasFor(annotation = FeignClient.class, attribute = "fallback")
    Class<?> fallback() default void.class;

    @AliasFor(annotation = FeignClient.class, attribute = "fallbackFactory")
    Class<?> fallbackFactory() default void.class;
    /**
     * Path prefix to be used by all method-level mappings. Can be used with or without {@code @RibbonClient}.
     * @return the path prefix to be used by all method-level mappings.
     */
    @AliasFor(annotation = FeignClient.class, attribute = "path")
    String path() default "";
}
