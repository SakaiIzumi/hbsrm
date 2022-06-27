package net.bncloud.bis.config;

import lombok.Data;
import net.bncloud.msk3cloud.config.K3CloudApiConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 金蝶配置类
 *
 * @author Rao
 * @Date 2022/01/15
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "k3cloud")
public class K3cloudConfiguration {

    /**
     *  也叫 DBID
     */
    private String acctId;

    /**
     * appId
     */
    private String appId;

    /**
     * app 密钥
     */
    private String appSecret;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 国际化ID
     */
    private Integer lcId = 2052;

    /**
     * 服务地址
     */
    private String serverUrl;

    /**
     * 构建配置
     * @return
     */
    public K3CloudApiConfig buildK3CloudApiConfig(){
        K3CloudApiConfig k3CloudApiConfig = new K3CloudApiConfig();
        BeanUtils.copyProperties( this, k3CloudApiConfig);
        return k3CloudApiConfig;
    }

}
