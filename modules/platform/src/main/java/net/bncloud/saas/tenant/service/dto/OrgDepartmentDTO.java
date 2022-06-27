package net.bncloud.saas.tenant.service.dto;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.convert.base.BaseDTO;
import net.bncloud.saas.tenant.domain.vo.ManagerVO;

import java.util.List;

/**
 * @ClassName OrdDepartmentDTO
 * @Description: OrdDepartmentDTO
 * @Author Administrator
 * @Date 2021/4/10
 * @Version V1.0
 **/
@Getter
@Setter
public class OrgDepartmentDTO<T extends OrgDepartmentDTO<?>> extends BaseDTO {
    private static final long serialVersionUID = 7716140368297983394L;
    private Long id;
    private String name;
    private String description;
    private T parent;
    private List<ManagerVO> managers;

}
