package net.bncloud.bis.task;

import net.bncloud.bis.srm.doc.manager.ContractFileManager;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.task.AbstractSchedulingConfigurer;
import net.bncloud.common.base.task.DynamicYamlTimedTaskConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class ContractFileSyncErpTask extends AbstractSchedulingConfigurer {

    public final static String TASK_KEY = "contractFileSyncErpTask";

    @Autowired
    private ContractFileManager contractFileManager;

    @Resource
    private DynamicYamlTimedTaskConfiguration taskConfiguration;

    protected ContractFileSyncErpTask(DistributedLock distributedLock) {
        super(distributedLock);
    }

    @Override
    protected String cron() {
        return taskConfiguration.getTaskNameCronMap().get( this.taskName() );
    }

    /*@Override
    protected void executeTask() {
        contractFileManager.syncContractFile();
    }*/
    @Override
    protected void executeTask() {
        contractFileManager.syncContractAndContractFile();
    }

    @Override
    protected String taskName() {
        return TASK_KEY;
    }
}
