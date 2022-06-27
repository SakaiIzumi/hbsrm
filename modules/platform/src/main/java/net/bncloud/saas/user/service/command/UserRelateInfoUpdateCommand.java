package net.bncloud.saas.user.service.command;


import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserRelateInfoUpdateCommand {
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "类型")
    private String subjectType;
    @ApiModelProperty(value = "主体Id")
    private Long subjectId;
    @ApiModelProperty(value = "岗位")
    private String position;
    @ApiModelProperty(value = "工号")
    private String jobNo;

    public UserRelateInfoUpdateCommand of(Long userId, String subjectType, Long subjectId, String postition, String jobNo) {
        return new UserRelateInfoUpdateCommand(userId, subjectType, subjectId, postition, jobNo);
    }
}
