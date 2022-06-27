package net.bncloud.quotation.vo;


import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
public class TRfqVerificationCodeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    List<TRfqVerificationCode> tRfqVerificationCode;

    String quotationId;

    String uid;

    String supplierCode;

    private String supplierName;

    String mobile;

    /**
     * 区分发送短信和站内信（1：短信,2：站内信）
     *
     * **/
    MessageTypeVo messageType;

    /**
     * 区分是哪一个人的短信验证码
     * purchaseUserId 采购
     * financialUserId 财务
     * auditUserId 审计
     *
     * **/
    String memberType;
}
