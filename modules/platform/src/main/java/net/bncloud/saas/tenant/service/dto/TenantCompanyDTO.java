package net.bncloud.saas.tenant.service.dto;

import net.bncloud.convert.base.BaseDTO;

/**
 * @ClassName CompanyDTO
 * @Description: 客户DTO
 * @Author Administrator
 * @Date 2021/4/13
 * @Version V1.0
 **/
public class TenantCompanyDTO extends BaseDTO {

    private Long id;

    private String code;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
