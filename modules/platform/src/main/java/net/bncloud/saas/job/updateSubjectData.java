package net.bncloud.saas.job;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.saas.authorize.service.DataAppConfigService;
import net.bncloud.saas.authorize.service.DesensitizeFieldMappingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class updateSubjectData {
    private final DesensitizeFieldMappingService desensitizeFieldMappingService;
    private final DataAppConfigService dataAppConfigService;



    public updateSubjectData(DesensitizeFieldMappingService desensitizeFieldMappingService, DataAppConfigService dataAppConfigService) {
        this.desensitizeFieldMappingService = desensitizeFieldMappingService;
        this.dataAppConfigService = dataAppConfigService;
    }

    /**
     * 定时更新数据权限配置
     */
    @Scheduled(cron = "0 0 0-22 * * ?")
    private void sync(){
        desensitizeFieldMappingService.load();
        dataAppConfigService.loadDataAppConfig();
    }
}
