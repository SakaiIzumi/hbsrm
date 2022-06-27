package net.bncloud.financial.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ContractOperateContent
 * @Description: 合同操作内容枚举
 * @Author Administrator
 * @Date 2021/3/16
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum StatementOperateType {
    //
    DELETE("statement_delete", "删除"),
    CUSTOMER_CONFIRM("statement_customer_confirm", "采购方确认"),
    SUPPLIER_CONFIRM("statement_supplier_confirm", "供应商确认"),
    WITHDRAW("statement_withdraw", "撤回"),
    EDIT("statement_edit", "编辑"),
    INVALID("statement_invalid", "作废"),
    REMIND("statement_remind", "提醒"),
    SEND("statement_send", "发送"),
    MAKE_INVOICE("statement_make_invoice", "开票"),
    REJECT("statement_reject", "退回"),
    ;

    private String code;

    private String name;
}
