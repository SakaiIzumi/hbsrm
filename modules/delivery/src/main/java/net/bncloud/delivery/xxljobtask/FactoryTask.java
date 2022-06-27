package net.bncloud.delivery.xxljobtask;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.delivery.constant.DeliveryTaskConstants;
import net.bncloud.delivery.service.FactoryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * desc: 工厂任务
 *
 * @author Rao
 * @Date 2022/06/17
 **/
@Slf4j
@Component
public class FactoryTask {

    @Autowired
    private FactoryInfoService factoryInfoService;

    /**
     * 同步采购方的信息到 工厂信息表
     * @return
     */
    @XxlJob(DeliveryTaskConstants.SYNC_PURCHASE_TO_FACTORY_INFO)
    public ReturnT<String> syncPurchaseToFactoryInfo() {
        factoryInfoService.syncPurchaseInfoToFactoryInfo();
        return ReturnT.SUCCESS;

    }

    public static void main(String[] args) {
        String s1="aaaaaa";
        String s2="";
        int i = s1.indexOf(s1);
        System.out.println(i);
    }

}
