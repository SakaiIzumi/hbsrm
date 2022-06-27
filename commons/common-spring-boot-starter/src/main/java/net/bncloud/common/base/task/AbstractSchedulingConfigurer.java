package net.bncloud.common.base.task;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.globallock.lock.LockWrapper;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务抽象
 * @author Rao
 * @Date 2021/11/27
 **/
@Slf4j
public abstract class AbstractSchedulingConfigurer implements SchedulingConfigurer {

    private final DistributedLock distributedLock;

    private static final String  SCHEDULING_TASK_PREFIX = "scheduling_task:";

    protected AbstractSchedulingConfigurer(DistributedLock distributedLock) {
        this.distributedLock = distributedLock;
    }

    /**
     * 获取时间表达式
     * @return
     */
    protected abstract String cron();

    /**
     * 执行任务
     */
    protected abstract void executeTask();

    /**
     * 任务名称
     * @return
     */
    protected abstract String taskName();

    private final Executor executor = new NioEventLoopGroup();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        taskRegistrar.setScheduler( executor);
        taskRegistrar.addTriggerTask(
            // 任务执行
            () -> {
                String key = SCHEDULING_TASK_PREFIX + this.taskName();
                LockWrapper lockWrapper = new LockWrapper().setKey(key).setWaitTime(0).setLeaseTime(10).setUnit(TimeUnit.SECONDS);
                distributedLock.tryLock( lockWrapper, () -> {
                    try {
                        long start = System.currentTimeMillis();
                        this.executeTask();
                        if( log.isInfoEnabled()){
                            log.info("[Scheduling task] {} execute success, seconds:{} !",this.taskName(),(System.currentTimeMillis() - start)/1000 );
                        }
                    } catch (Exception ex) {
                        log.error("[Scheduling task] {} execute fail! ", this.taskName(), ex);
                        return false;
                    }
                    return true;
                }, () -> {
                    if( log.isDebugEnabled()){
                        log.debug("[Scheduling task] {},other node has executed! ",this.taskName());
                    }
                    return false;
                });

            },
            // 加载动态配置
            (triggerContext) -> {
                try {
                    return new CronTrigger( this.cron()).nextExecutionTime( triggerContext);
                }catch (Exception ex){
                    log.error("[Scheduling task] {} start fail !",this.taskName(), ex);
                    return null;
                }
            }
        );
        if( log.isInfoEnabled()){
            log.info("[Scheduling task] {} start success ",this.taskName());
        }

    }
}
