package net.bncloud.saas.tenant.service.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName OrgEmployeeQuery
 * @Description: 部门员工查询对象
 * @Author Administrator
 * @Date 2021/4/22
 * @Version V1.0
 **/
@Getter
@Setter
public class OrgEmployeeQuery {

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 员工号
     */
    private String jobNo;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 启用状态
     */
    private Boolean enabled;

    /**
     * 根据姓名、员工号、手机号模糊搜索
     */
    private String qs;

    /**
     * 采购方编码
     */
    private String purchaserCode;

    private Long orgId;

}
