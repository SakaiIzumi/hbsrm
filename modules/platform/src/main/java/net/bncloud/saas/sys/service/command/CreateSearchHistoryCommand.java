package net.bncloud.saas.sys.service.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.security.Platform;
import net.bncloud.saas.sys.domain.BncModuleType;
import net.bncloud.saas.sys.domain.SearchHistory;
import net.bncloud.saas.tenant.domain.OrgDepartment;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.domain.vo.UserId;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName CreateSearchHistoryCommand
 * @Description: 搜索历史
 * @Author Administrator
 * @Date 2021/4/27
 * @Version V1.0
 **/
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class CreateSearchHistoryCommand {

    @ApiModelProperty(value = "公司ID")
    private Long companyId;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "平台")
    private Platform platform;
    @NotNull(message = "模块不能为空")
    @ApiModelProperty(value = "模块")
    private BncModuleType module;
    @NotBlank(message = "搜索值不能为空")
    @ApiModelProperty(value = "搜索值")
    private String value;

    public SearchHistory create(){
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setCompanyId(companyId);
        searchHistory.setUserId(userId);
        searchHistory.setPlatform(platform);
        searchHistory.setModule(module);
        searchHistory.setValue(value);
        return  searchHistory;
    }

}
