package net.bncloud.quotation.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 短信验证码信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-07
 */
@Data


public class VerificationCodeVo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String verificationCode;


    /**
     * 手机号对应的用户的uid
     */
    @ApiModelProperty(value = "uid")
    private Long uid;

}
