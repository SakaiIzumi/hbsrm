package net.bncloud.baidu.properties;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.baidu.model.enums.AuthCheck;
import net.bncloud.baidu.model.enums.Output;
import net.bncloud.common.exception.Asserts;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * desc:
 *   使用ak密钥的话是使用白名单的方式来访问。
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@Slf4j
@RefreshScope(proxyMode = ScopedProxyMode.NO)
@Setter
@Getter
@ConfigurationProperties(prefix = "application.baidu-map")
public class BaiduMapProperties implements InitializingBean {


    /**
     * 域名
     */
    private String url;

    /**
     * ak 密钥 ，百度开发平台注册并填写。https://lbsyun.baidu.com/index.php?title=%E9%A6%96%E9%A1%B5
     */
    private String ak;

    /**
     * sn 校验方式
     */
    private String sk;

    /**
     * 输出 xml/json
     */
    private Output output = Output.json;

    private AuthCheck authCheck = AuthCheck.ip;

    @Override
    public void afterPropertiesSet() throws Exception {
        String logStr = "[百度地图]初始化配置信息提醒，";
        log.info( logStr + this.authCheck.desc() );

        Asserts.isTrue( StrUtil.isNotBlank( this.ak ),"ak 参数未填写。" );

        if( AuthCheck.isSn( this.authCheck )){
            Asserts.isTrue( StrUtil.isNotBlank( this.sk ),"sn 认证方式，必须填写sk参数！");
        }

    }
}
