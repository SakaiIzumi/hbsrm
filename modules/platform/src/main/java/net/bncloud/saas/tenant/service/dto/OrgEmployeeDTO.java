package net.bncloud.saas.tenant.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import net.bncloud.common.util.DateUtil;
import net.bncloud.convert.base.BaseDTO;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.service.dto.RoleDTO;
import net.bncloud.saas.tenant.domain.OrgDepartment;
import net.bncloud.saas.tenant.domain.OrgEmployeeMangeScope;
import net.bncloud.saas.tenant.domain.vo.UserId;
import net.bncloud.saas.user.domain.UserInfo;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName OrgEmployeeDTO
 * @Description: 员工DTO
 * @Author Administrator
 * @Date 2021/4/25
 * @Version V1.0
 **/
@Data
public class OrgEmployeeDTO extends BaseDTO {

    private Long id;

    private String code;

    private String name;

    private UserId user;

    private String jobNo;

    @JsonIgnoreProperties("employees")
    private OrgDepartment department;

    private String mobile;

    /**
     * 是否启用
     */
    private boolean enabled;

    private UserInfo userInfo;

    private String position;

    private Long createdBy;

    private String createdByName;

    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private Date createdDate;

    private Long orgId;

    private String orgName;

    private List<Long> roleIds;

    private List<RoleDTO> roles;

    private String orgEmployeeMangeScope;

}
