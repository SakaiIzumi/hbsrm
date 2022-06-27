package net.bncloud.saas.event.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.saas.event.CreateOrganization;
import net.bncloud.saas.event.OrganizationCreateEvent;
import net.bncloud.saas.tenant.domain.OrgManagerRecord;
import net.bncloud.saas.tenant.domain.OrganizationRecord;
import net.bncloud.saas.tenant.service.OrganizationRecordService;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
@Slf4j
public class OrgCreateOrgManagerRecordListener implements SmartApplicationListener {
    private final OrganizationRecordService organizationRecordService;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        OrganizationCreateEvent organizationCreateEvent = (OrganizationCreateEvent) applicationEvent;
        CreateOrganization createOrganization = organizationCreateEvent.getCreateOrganization();
        createOrganization.getUserId();
        Long orgId = createOrganization.getOrgId();
        String orgName = createOrganization.getOrgName();
        Long orgManagerRecordId = createOrganization.getOrgManagerRecordId();
        //记录创建协助组织
        OrgManagerRecord orgManagerRecord = new OrgManagerRecord();
        orgManagerRecord.setId(orgManagerRecordId);
        OrganizationRecord record = OrganizationRecord.builder()
                .orgId(orgId)
                .orgName(orgName)
                .orgManagerRecord(orgManagerRecord)
                .build();
        organizationRecordService.save(record);
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        return OrganizationCreateEvent.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    @Override
    public int getOrder() {
        return 1; //需要保证顺序执行
    }


}
