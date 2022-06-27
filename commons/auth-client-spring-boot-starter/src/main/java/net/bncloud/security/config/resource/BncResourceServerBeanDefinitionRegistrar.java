package net.bncloud.security.config.resource;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class BncResourceServerBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /***
     * 资源服务器默认bean名称
     */
    String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurer";

    /**
     * 根据注解值动态注入资源服务器的相关属性
     *
     * @param metadata 注解信息
     * @param registry 注册器
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder beanDefinitionBuilder =
                BeanDefinitionBuilder.genericBeanDefinition(BncResourceServerConfigurerAdapter.class);
        registry.registerBeanDefinition(RESOURCE_SERVER_CONFIGURER, beanDefinitionBuilder.getBeanDefinition());
    }
}
