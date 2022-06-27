package net.bncloud.quotation.vo;


import lombok.Data;
import net.bncloud.quotation.entity.TRfqVerificationCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 短信验证码信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-07
 */
@Data
public class ConfirmVerificationCodeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    List<VerificationCodeVo> verificationCodeVo;

    String quotationId;



}
