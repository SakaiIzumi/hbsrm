package net.bncloud.bis.config;

import net.bncloud.msk3cloud.client.K3CloudApiClient;
import net.bncloud.msk3cloud.core.Intercept.LoginInvalidationIntercept;
import net.bncloud.msk3cloud.kingdee.K3cloudRemoteService;
import net.bncloud.msk3cloud.kingdee.K3cloudRemoteServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 金蝶配置 bean类
 *
 * @author Rao
 * @Date 2022/01/17
 **/
@Configuration
public class K3cloudBeanConfig {

    /**
     * 配置远程调用的 客户端
     * @param k3cloudConfiguration
     * @return
     */
    @Bean
    public K3CloudApiClient k3CloudApiClient( K3cloudConfiguration k3cloudConfiguration ){
        return new K3CloudApiClient( k3cloudConfiguration.buildK3CloudApiConfig() );
    }

    /**
     * 配置调用服务
     * @param k3CloudApiClient
     * @return
     */
    @Bean
    public K3cloudRemoteService baseService(K3CloudApiClient k3CloudApiClient ){
        return new K3cloudRemoteServiceImpl( k3CloudApiClient );
    }

    /**
     * 注入登录拦截器
     * @param k3CloudApiClient
     * @return
     */
    @Bean
    public LoginInvalidationIntercept loginInvalidationIntercept( K3CloudApiClient k3CloudApiClient ){
        return new LoginInvalidationIntercept( k3CloudApiClient );
    }

}
