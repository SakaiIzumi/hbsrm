package net.bncloud.saas.event.listener;

import net.bncloud.saas.ding.service.DingTalkDeptService;
import net.bncloud.saas.event.CreatedOrgDept;
import net.bncloud.saas.event.OrgSupplierDeptCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SaaSApplicationEventListener {

    private final DingTalkDeptService dingTalkDeptService;

    public SaaSApplicationEventListener(DingTalkDeptService dingTalkDeptService) {
        this.dingTalkDeptService = dingTalkDeptService;
    }

    @Async
    @EventListener(OrgSupplierDeptCreatedEvent.class)
    public void orgSupplierDeptCreatedEventListener(OrgSupplierDeptCreatedEvent event) {

        CreatedOrgDept dept = event.getCreatedOrgDept();
        dingTalkDeptService.createSupplierDept(dept.getOrgId(), dept.getDeptId(), dept.getDeptName());
    }
}
