package net.bncloud.saas.user.strategy.switchsub;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.security.BncUserDetails;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.BeanUtilTwo;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.repository.SupplierRepository;
import net.bncloud.saas.supplier.repository.SupplierStaffRepository;
import net.bncloud.saas.user.domain.UserCurrent;
import net.bncloud.saas.user.domain.vo.SupplierVO;
import net.bncloud.saas.user.repository.UserCurrentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("supplierSwitchCurrentSubjectStrategy")
@AllArgsConstructor
public class SupplierSwitchCurrentSubjectStrategy implements ISwitchCurrentSubjectStrategy {
    private final SupplierStaffRepository supplierStaffRepository;
    private final SupplierRepository supplierRepository;
    private final UserCurrentRepository userCurrentRepository;

    @Override
    public SwitchCurrentUserInfo switchCurrentSubject(Long subId) {
        SwitchCurrentUserInfo switchCurrentUserInfo = new SwitchCurrentUserInfo();
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        Optional<BncUserDetails> bncUserDetailsOptional = SecurityUtils.getCurrentUser();
        loginInfo.setCurrentSubjectType(SubjectType.supplier.name());
        Optional<Supplier> supplierOptional = supplierRepository.findById(subId);
        if (!supplierOptional.isPresent()) {
            return switchCurrentUserInfo;
        }
        net.bncloud.saas.supplier.domain.Supplier supplier = supplierOptional.get();
        net.bncloud.common.security.Supplier currentSupplier = loginInfo.getCurrentSupplier();
        currentSupplier.setSupplierId(supplier.getId());
        currentSupplier.setSupplierName(supplier.getName());
        currentSupplier.setSupplierCode(supplier.getCode());
        loginInfo.setCurrentSupplier(currentSupplier);
        Optional<SupplierStaff> supplierStaffOptional = supplierStaffRepository.findAllByUser_UserIdAndSupplier_Id(loginInfo.getId(), currentSupplier.getSupplierId());
        if (supplierStaffOptional.isPresent()) {
            SupplierStaff supplierStaff = supplierStaffOptional.get();
            List<Role> roles = supplierStaff.getRoles();
            updateLoginInfoRoles(loginInfo, roles);
        } else {
            updateLoginInfoRoles(loginInfo, Lists.newArrayList());
        }
        switchCurrentUserInfo.setLoginInfo(loginInfo);
        if (bncUserDetailsOptional.isPresent()) {
            BncUserDetails bncUserDetails = bncUserDetailsOptional.get();
            UserCurrent userCurrent = userCurrentRepository.findById(bncUserDetails.getId()).get();
            if (BeanUtilTwo.isNotEmpty(userCurrent)) {
                userCurrent.setCurrentSupplier(SupplierVO.of(supplier.getId(), supplier.getCode(), supplier.getName()));
                userCurrentRepository.save(userCurrent);
                switchCurrentUserInfo.setUserCurrent(userCurrent);
            }
        }
        return switchCurrentUserInfo;
    }

    //更改当前用户角色
    private void updateLoginInfoRoles(LoginInfo loginInfo, List<net.bncloud.saas.authorize.domain.Role> roles) {
        Set<net.bncloud.common.security.Role> rolesSet = loginInfo.getRoles();
        if (CollectionUtil.isNotEmpty(roles)) {
            if (CollectionUtil.isEmpty(rolesSet)) {
                rolesSet = Sets.newHashSet();
            }
            List<net.bncloud.common.security.Role> collect = roles.stream().map(r -> {
                net.bncloud.common.security.Role role = new net.bncloud.common.security.Role();
                role.setId(r.getId());
                role.setName(r.getName());
                role.setSysDefault(r.isSysDefault());
                return role;
            }).collect(Collectors.toList());
            rolesSet.addAll(collect);
            loginInfo.setRoles(rolesSet);
        }
    }
}
