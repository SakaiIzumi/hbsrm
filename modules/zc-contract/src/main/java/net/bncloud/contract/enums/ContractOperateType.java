package net.bncloud.contract.enums;

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
public enum ContractOperateType {
    SEND("contract_send","发送"),
    EDIT("contract_edit","编辑"),
    REMIND("contract_remind","提醒"),
    WITHDRAW("contract_withdraw","撤回"),
    RE_SIGN("contract_re_sign","重新签约"),
    UPDATE("contract_update","更新合同"),
    INVALID("contract_invalid","作废"),
    RE_INITIATE("contract_re_initiate","重新发起"),
    REJECT("contract_reject","拒绝"),
    CONFIRM("contract_confirm","确认"),
    ;

    private String code;

    private String name;
}
