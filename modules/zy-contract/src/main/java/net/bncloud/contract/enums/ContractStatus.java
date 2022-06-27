package net.bncloud.contract.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName ContractStatus
 * @Description: 合同状态枚举类
 * @Author Administrator
 * @Date 2021/3/16
 * @Version V1.0
 **/
public enum ContractStatus {

    DRAFT("1","草稿","草稿"),
    TO_BE_ANSWERED("2","待答交","待确认"),
    REJECTED("3","被拒","被拒"),
    TO_BE_SIGNED_ONLINE("4","待网签","待网签"),
    ABNORMAL("5","异常","异常"),
    VALID("6","有效","有效"),
    INVALID("7","无效","无效"),
    EXPIRED("8","过期","过期"),
    WITHDRAWN("9","已撤回","已撤回"),
    UPDATING("10","更新中","更新中"),
    ;

    ContractStatus(String code,String name,String alias){
        this.code = code;
        this.name = name;
        this.alias = alias;
    }

    /**
     * 通过code取枚举
     * @param code
     * @return
     */
    public static ContractStatus getTypeByCode(String code){
        if (StringUtils.isBlank(code)){
            return null;
        }
        for (ContractStatus enums : ContractStatus.values()) {
            if (StringUtils.equals(enums.getCode(),code)){
                return enums;
            }
        }
        return null;
    }

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 别名
     */
    private String alias;


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
}
