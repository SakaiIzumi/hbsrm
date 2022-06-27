package net.bncloud.security.annotation;

import net.bncloud.security.config.SecurityConfiguration;
import net.bncloud.security.config.resource.BncResourceServerBeanDefinitionRegistrar;
import net.bncloud.security.exception.DefaultSecurityExceptionHandlerConfig;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableResourceServer
@Import({SecurityConfiguration.class,
        DefaultSecurityExceptionHandlerConfig.class,
        BncResourceServerBeanDefinitionRegistrar.class})
public @interface EnableBncResourceServer {
}
