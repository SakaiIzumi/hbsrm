package net.bncloud.msk3cloud.config;

import lombok.Data;

/**
 * {@link com.kingdee.bos.webapi.sdk.CfgUtil#getAppDefaultCfg}
 * @author Rao
 * @Date 2022/01/06
 **/
@Data
public class K3CloudApiConfig {

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

}
