package net.bncloud.financial.enums;

import lombok.Getter;

/**
 * @author Rao
 * @Date 2021/12/25
 **/
@Getter
public enum AttachmentBizType {

    /**
     * 对账单
     */
    statement(0),
    /**
     * 发票通知单
     */
    invoice_note(1),

    /**
     * 费用单
     */
    COST_BILL(2),
    ;

    private final Integer bizType;

    AttachmentBizType(Integer bizType) {
        this.bizType = bizType;
    }

}
