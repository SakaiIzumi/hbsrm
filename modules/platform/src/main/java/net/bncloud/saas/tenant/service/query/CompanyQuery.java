package net.bncloud.saas.tenant.service.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName CompanyQuery
 * @Description: 客户查询对象
 * @Author Administrator
 * @Date 2021/4/13
 * @Version V1.0
 **/
@Getter
@Setter
public class CompanyQuery {

    private String code;
    private String name;
    private String qs;
}
