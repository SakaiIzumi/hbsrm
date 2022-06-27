package net.bncloud.delivery.xxljobtask;

import cn.hutool.core.util.StrUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.delivery.constant.DeliveryTaskConstants;
import net.bncloud.delivery.feign.HolidayFeignClient;
import net.bncloud.delivery.service.HolidayDateService;
import net.bncloud.delivery.vo.apihub.ApiHubResult;
import net.bncloud.delivery.vo.apihub.HolidayParam;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * desc: 假日日期作业任务
 *
 * @author Rao
 * @Date 2022/05/19
 **/
@Slf4j
@Component
public class HolidayDateJobTask {

    private final HolidayFeignClient holidayFeignClient;
    @Resource
    private HolidayDateService holidayDateService;

    public HolidayDateJobTask(HolidayFeignClient holidayFeignClient) {
        this.holidayFeignClient = holidayFeignClient;
    }

    /**
     * 节假日数据同步
     * @return
     */
    @XxlJob("holidayDateSyncTask")
    public ReturnT<String> holidayDateSyncTask(){
        //传入年份
        String param = XxlJobHelper.getJobParam();
        HolidayParam holidayParam = HolidayParam.builder().build();
        if(StrUtil.isNotBlank( param )){
            int year = Integer.parseInt(param);
            holidayParam.setYear( year );
        }

        ApiHubResult<ApiHubResult.PageInfo<ApiHubResult.DateInfo>> apiHubResult = holidayFeignClient.getHolidays( holidayParam );
        if( apiHubResult.requestFail() ){
            return new ReturnT<>(ReturnT.FAIL_CODE, apiHubResult.getMsg());
        }

        ApiHubResult.PageInfo<ApiHubResult.DateInfo> apiHubResultPageData = apiHubResult.getData();
        if( apiHubResultPageData == null || CollectionUtils.isEmpty( apiHubResultPageData.getList() ) ){
            return new ReturnT<>(ReturnT.FAIL_CODE,"请求节假日数据结果为空，查看 holidayDateSyncTask 的调用！");
        }

        List<ApiHubResult.DateInfo> dateInfos = apiHubResultPageData.getList();
        holidayDateService.batchSaveDateInfoList( holidayParam.getYear() ,dateInfos);

        return ReturnT.SUCCESS;
    }

}
