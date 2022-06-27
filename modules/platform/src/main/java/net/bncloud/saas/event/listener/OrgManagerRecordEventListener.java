package net.bncloud.saas.event.listener;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.domain.RoleGroup;
import net.bncloud.saas.authorize.domain.User;
import net.bncloud.saas.authorize.repository.RoleGroupRepository;
import net.bncloud.saas.authorize.service.UserService;
import net.bncloud.saas.event.CreateOrgManagerRecord;
import net.bncloud.saas.event.CreatedUser;
import net.bncloud.saas.event.OrgManagerRecordCreateEvent;
import net.bncloud.saas.event.OrgManagerRecordDeleteEvent;
import net.bncloud.saas.tenant.domain.OrgManagerRecord;
import net.bncloud.saas.tenant.domain.vo.UserVO;
import net.bncloud.saas.tenant.service.OrgManagerRecordService;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.service.UserInfoService;
import net.bncloud.saas.user.service.command.UserCreateCommand;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
@AllArgsConstructor
public class OrgManagerRecordEventListener {

    private final UserInfoService userInfoService;
    private final UserService userService;
    private final OrgManagerRecordService orgManagerRecordService;
    private final RoleGroupRepository roleGroupRepository;

    @EventListener(OrgManagerRecordCreateEvent.class)
    public void createOrgManagerRecord(OrgManagerRecordCreateEvent orgManagerRecordCreateEvent) {
        CreateOrgManagerRecord createOrgManagerRecord = orgManagerRecordCreateEvent.getCreateOrgManagerRecord();
        UserInfo userInfo = userInfoService.getUserByMobile(createOrgManagerRecord.getMobile(), "");
        if (userInfo == null) {
            userInfo = userInfoService.createActiveUser(UserCreateCommand.of(createOrgManagerRecord.getName(), createOrgManagerRecord.getMobile(), "86", "swy20210401")); // fixme
        }
        Optional<User> userOptional = userService.findById(userInfo.getId());
        User user = null;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = userService.createUser(new CreatedUser(userInfo.getId(), userInfo.getName(), userInfo.getMobile()));
        }
        //初始创建协助组织管理员,为普通平台角色
        RoleGroup roleGroup = roleGroupRepository.findOneByName("平台协作组织管理员组"); //TODO 抽离到配置,
        ArrayList<Role> roles = Lists.newArrayList(roleGroup.getRoles());
        user.setRoles(roles);
        userService.save(user);
        //暂定是ORG
        OrgManagerRecord orgManagerRecord = this.build(userInfo.getId(), userInfo.getName(), userInfo.getCode(), "ORG", createOrgManagerRecord.getMobile(), createOrgManagerRecord.getEnabled());
        orgManagerRecordService.save(orgManagerRecord);
    }

    @EventListener(OrgManagerRecordDeleteEvent.class)
    public void deleteOrgManagerRecord(OrgManagerRecordDeleteEvent orgManagerRecordDeleteEvent) {
        Long userId = orgManagerRecordDeleteEvent.getDeleteOrgManagerRecord().getUserId();
        //删除相关的角色关系
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isPresent()) {
            RoleGroup roleGroup = roleGroupRepository.findOneByName("平台协作组织管理员组"); //TODO 抽离到配置,
            ArrayList<Role> roles = Lists.newArrayList(roleGroup.getRoles());
            User user = userOptional.get();
            user.getRoles().removeAll(roles);
        }
    }

    private OrgManagerRecord build(Long userId, String userName, String code, String managerType, String mobile, boolean enabled) {
        return OrgManagerRecord
                .builder()
                .code(code)
                .user(UserVO.of(userId, userName))
                .enabled(enabled)
                .managerType(managerType)
                .mobile(mobile)
                .build();
    }
}
