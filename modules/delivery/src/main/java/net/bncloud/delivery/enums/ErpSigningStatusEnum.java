package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ErpSigningStatusEnum
 * @Description: ERP签收状态
 * @Author Administrator
 * @Date 2021/4/12
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum ErpSigningStatusEnum {
    NOT_SIGNED("not_signed","未签收"),
    SIGNED("signed","已签收"),
    ;

    private String code;

    private String name;
}
