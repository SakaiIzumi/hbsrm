package net.bncloud.bis.xxljobtask;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.manager.ExperimentSupplierManager;
import net.bncloud.common.api.R;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.service.api.platform.config.ConfigParamOpenFeign;
import net.bncloud.service.api.platform.config.enums.CfgParamKeyEnum;
import net.bncloud.service.api.platform.config.vo.CfgParamInfo;
import net.bncloud.service.api.platform.config.vo.configvo.DeliveryCollaborationMethod;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * desc: mrp结果同步
 *
 * @author Rao
 * @date 2022/05/23
 **/
@Slf4j
@Component
public class MrpResultSyncTask {

    @Resource
    private ExperimentSupplierManager experimentSupplierManager;

    @Resource
    private ConfigParamOpenFeign configParamOpenFeign;

    /**
     * 同步计划订单 （不需要定时任务了）
     * @return
     */
//    @XxlJob( BisSyncConstants.PLAN_ORDER_SYNC_ERP_TASK )
    public ReturnT<String> syncMrpPlanOrder(){

        // 查询送货协同方式配置
        R<CfgParamInfo> cfgParamInfoR = configParamOpenFeign.findListByCodeAndOrgId(CfgParamKeyEnum.DELIVERY_DELIVERY_COLLABORATION_METHOD, 112L);
        Assert.isTrue( cfgParamInfoR.isSuccess() && cfgParamInfoR.getData() != null,"获取送货协同方式配置失败！" );
        DeliveryCollaborationMethod deliveryCollaborationMethod = JSON.parseObject( cfgParamInfoR.getData().getValue(), DeliveryCollaborationMethod.class);

        log.info("[计划订单同步] deliveryCollaborationMethod ：{}", JSON.toJSONString( deliveryCollaborationMethod));
        if( deliveryCollaborationMethod.planSchedulingValue() ) {

            // 查询是否自动同步MRP需求计划的配置
            cfgParamInfoR = configParamOpenFeign.findListByCodeAndOrgId(CfgParamKeyEnum.DELIVERY_AUTOMATICALLY_SYNCHRONIZE_MRP_DEMAND_PLANS, 112L);
            Assert.isTrue( cfgParamInfoR.isSuccess() && cfgParamInfoR.getData() != null,"查询是否自动同步MRP需求计划的配置失败！" );

            log.info("[计划订单同步] 查询是否自动同步MRP需求计划的配置:{}", cfgParamInfoR.getData().getValue() );
            if (Boolean.parseBoolean( cfgParamInfoR.getData().getValue() )) {
                R<Object> syncR = experimentSupplierManager.experimentSupplierSyncMrpPlanOrder(XxlJobHelper.getJobParam());
                XxlJobHelper.log( "result:{}",JSON.toJSONString( syncR ) );
                log.info("result:{}",JSON.toJSONString( syncR ));
            }

        }

        return ReturnT.SUCCESS;

    }

}
