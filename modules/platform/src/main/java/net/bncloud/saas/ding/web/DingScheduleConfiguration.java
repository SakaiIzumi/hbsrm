package net.bncloud.saas.ding.web;

import net.bncloud.saas.ding.service.DingTalkSyncDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Configuration
public class DingScheduleConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(DingScheduleConfiguration.class);
    @Resource
    private DingTalkSyncDataService dingTalkSyncDataService;

//    //定时同步组织架构数据到钉钉平台
//    @Async
//    @Scheduled (cron = "0/15 * * * * ?")
//    public void syncDataToDing(){
//        LOGGER.info("定时同步组织架构数据到钉钉平台开始");
//        try {
//            dingTalkSyncDataService.syncData();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {}
//        LOGGER.info("定时同步组织架构数据到钉钉平台结束");
//    }
}