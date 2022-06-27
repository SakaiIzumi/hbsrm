package net.bncloud.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/1/25
 */
@Getter
@AllArgsConstructor
public enum ContractCfgParam {

    CONTRACT_AUTO_SEND("contract:contract_net_sign","合同是否自动发送"),
    ;
    private String code;
    private String description;
}
