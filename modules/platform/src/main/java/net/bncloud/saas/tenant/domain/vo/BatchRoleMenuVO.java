package net.bncloud.saas.tenant.domain.vo;

import lombok.Data;
import net.bncloud.saas.authorize.domain.vo.DimensionGrantVO;

import java.util.List;

@Data
public class BatchRoleMenuVO {
    private Long id;

    private String name;

    private Boolean enabled;

    private List<Long> menuIds;

    /**
     * 数据权限
     */
    private List<DimensionGrantVO> purGrants;

    private List<DimensionGrantVO> supGrants;

    private List<DimensionGrantVO> amountGrants;
}
