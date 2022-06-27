package net.bncloud.bis.task;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.manager.PurchaseOrderManager;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.task.AbstractSchedulingConfigurer;
import net.bncloud.common.base.task.DynamicYamlTimedTaskConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * desc: 采购订单数据同步任务
 *
 * @author Rao
 * @Date 2022/01/17
 **/
@Deprecated
@Slf4j
//@Component
public class PurchaseOrderSyncErpTask extends AbstractSchedulingConfigurer {

    public final static String TASK_KEY = "PurchaseOrderSyncErpTask";

    @Resource
    private DynamicYamlTimedTaskConfiguration dynamicYamlTimedTaskConfiguration;

    @Autowired
    private PurchaseOrderManager purchaseOrderManager;

    protected PurchaseOrderSyncErpTask(DistributedLock distributedLock) {
        super(distributedLock);
    }

    @Override
    protected String cron() {
        return dynamicYamlTimedTaskConfiguration.getTaskNameCronMap().get( this.taskName() );
    }

    @Override
    protected void executeTask() {
        purchaseOrderManager.syncData(0);
    }

    @Override
    protected String taskName() {
        return TASK_KEY;
    }
}
