package net.bncloud.service.api.platform.tenant.enums;

import lombok.Getter;

@Getter
public enum ScopeEnum {
    SELF("self","本人"),
    ORG_ALL("org_all","全组织");

    private String code;
    private String name;

     ScopeEnum(String code ,String name){
        this.code=code;
        this.name =name;
    }
}
