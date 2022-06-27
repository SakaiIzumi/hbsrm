package net.bncloud.bis.task;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.manager.SupplierManager;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.task.AbstractSchedulingConfigurer;
import net.bncloud.common.base.task.DynamicYamlTimedTaskConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * desc: 供应商信息同步定时任务
 *
 * @author Rao
 * @Date 2022/01/17
 **/
@Deprecated
@Slf4j
//@Component
public class SupplierSyncOaTask extends AbstractSchedulingConfigurer {

    public final static String TASK_KEY = "SupplierSyncOaTask";

    @Autowired
    private SupplierManager supplierManager;

    @Resource
    private DynamicYamlTimedTaskConfiguration taskConfiguration;

    protected SupplierSyncOaTask(DistributedLock distributedLock) {
        super(distributedLock);
    }

    @Override
    protected String cron() {
        return taskConfiguration.getTaskNameCronMap().get( this.taskName() );
    }

    @Override
    protected void executeTask() {
        supplierManager.syncSupplierInfo( null );
    }

    @Override
    protected String taskName() {
        return TASK_KEY;
    }
}
