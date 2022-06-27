package net.bncloud.saas.event.listener;

import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.saas.event.UserRelateInfoUpdate;
import net.bncloud.saas.event.UserRelateInfoUpdateEvent;
import net.bncloud.saas.supplier.service.SupplierStaffService;
import net.bncloud.saas.tenant.service.OrgEmployeeService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserRelateInfoUpdateEventListener {

    private final OrgEmployeeService orgEmployeeService;

    private final SupplierStaffService supplierStaffService;


    @EventListener(UserRelateInfoUpdateEvent.class)
    public void orgSupplierDeptCreatedEventListener(UserRelateInfoUpdateEvent event) {
        UserRelateInfoUpdate userRelateInfoUpdate = event.getUserRelateInfoUpdate();
        updateEmp(userRelateInfoUpdate);
        updateStaff(userRelateInfoUpdate);
    }


    public void updateEmp(UserRelateInfoUpdate userRelateInfoUpdate) {
        if (SubjectType.org.name().equals(userRelateInfoUpdate.getSubjectType())) {
            orgEmployeeService.updateInfo(
                    userRelateInfoUpdate.getUserId()
                    , userRelateInfoUpdate.getSubjectId()
                    , userRelateInfoUpdate.getPosition()
                    , userRelateInfoUpdate.getJobNo());
        }
    }


    public void updateStaff(UserRelateInfoUpdate userRelateInfoUpdate) {
        if (SubjectType.supplier.name().equals(userRelateInfoUpdate.getSubjectType())) {
            supplierStaffService.updateInfo(
                    userRelateInfoUpdate.getUserId()
                    , userRelateInfoUpdate.getSubjectId()
                    , userRelateInfoUpdate.getPosition()
                    , userRelateInfoUpdate.getJobNo());
        }

    }

}
