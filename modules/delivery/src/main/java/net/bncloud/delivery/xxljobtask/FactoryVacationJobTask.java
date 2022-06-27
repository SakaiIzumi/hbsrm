package net.bncloud.delivery.xxljobtask;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.delivery.service.FactoryVacationService;
import org.springframework.stereotype.Component;

/**
 * desc: 工厂假期任务
 *
 * @author liyh
 * @Date 2022/05/23
 **/
@Slf4j
@Component
public class FactoryVacationJobTask {

    private FactoryVacationService factoryVacationService;

    public FactoryVacationJobTask(FactoryVacationService factoryVacationService) {
        this.factoryVacationService = factoryVacationService;
    }

    /**
     * 定时清除过期的假日数据 凌晨执行一天(? 或者时间间隔长一点)清除一次 清除当天凌晨之前的假日数据
     *
     * 后面的可能优化会做成日历,所以暂时不确定需不需要删除过期的数据
     *
     * 但是按照目前  是可以删除的
     *
     * 但是考虑到用户可能会回查以前的法定和手动添加的 所以只删除默认非工作日
     *
     * @return
     */
    @XxlJob("clearOverdueDate")
    public ReturnT<String> clearOverdueDate(){

        XxlJobHelper.log("clearOverdueDate mission start");

        //重新计算
        factoryVacationService.clearOverdueDate();

        XxlJobHelper.log("clearOverdueDate mission end");
        return ReturnT.SUCCESS;

    }

    /**
     * 补全假期的任务
     *
     * 比如 现在已经过了半年  没有对系统进行操作 那就只有半年的假期显示了
     *
     * 这个任务可以把剩下半年的假期  比如周末  补全
     *
     * 凌晨执行 一天一次
     *
     * 确实有这个可能  但是现在时间比较紧张  这个当成优化以后再说吧
     *
     * @return
     */
    @XxlJob("completionVacation")
    public ReturnT<String> completionVacation(){

        XxlJobHelper.log("completionVacation mission start");

        //补全
        //factoryVacationService.completionVacation();

        XxlJobHelper.log("completionVacation mission end");
        return ReturnT.SUCCESS;

    }

}
