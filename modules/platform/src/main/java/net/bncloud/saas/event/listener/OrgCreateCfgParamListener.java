package net.bncloud.saas.event.listener;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.saas.event.OrganizationCreateEvent;
import net.bncloud.saas.sys.domain.CfgParam;
import net.bncloud.saas.sys.domain.CfgParamMould;
import net.bncloud.saas.sys.service.CfgParamMouldService;
import net.bncloud.saas.sys.service.CfgParamService;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class OrgCreateCfgParamListener implements SmartApplicationListener {

    private final CfgParamService cfgParamService;
    private final CfgParamMouldService cfgParamMouldService;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        OrganizationCreateEvent organizationCreateEvent = (OrganizationCreateEvent) applicationEvent;
        Long orgId = organizationCreateEvent.getCreateOrganization().getOrgId();
        List<CfgParamMould> cfgParamMoulds = cfgParamMouldService.queryAll();
        List<CfgParam> cfgParamList = cfgParamMoulds.stream().map(cfgParamMould -> {
            CfgParam cfgParam = new CfgParam();
            cfgParam.setOrgId(orgId);
            BeanUtil.copyProperties(cfgParamMould, cfgParam, "id");
            return cfgParam;
        }).collect(Collectors.toList());
        cfgParamService.createAll(cfgParamList);
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
        return 3; //需要保证顺序执行
    }


}
