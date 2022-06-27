package net.bncloud.delivery.feign;

import net.bncloud.delivery.feign.fallback.HolidayFeignClientFallbackFactory;
import net.bncloud.delivery.vo.apihub.ApiHubResult;
import net.bncloud.delivery.vo.apihub.HolidayParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * desc: 国内法定节假日查询接口
 *  接口坞：http://www.apihubs.cn/#/holiday
 *  文档：https://blog.csdn.net/u012981882/article/details/112552450
 *  调用次数限制：100次/1分钟
 *  https://api.apihubs.cn/holiday/get
 * @author Rao
 * @Date 2022/05/19
 **/
@FeignClient(name = "holidayFeignClient",url = "https://api.apihubs.cn/holiday",fallbackFactory = HolidayFeignClientFallbackFactory.class,configuration = FeignClientsConfiguration.class)
public interface HolidayFeignClient {

    /**
     * 查询一整年的日期数据
     * @return
     */
    @GetMapping("/get")
    ApiHubResult<ApiHubResult.PageInfo<ApiHubResult.DateInfo>> getHolidays(@SpringQueryMap HolidayParam holidayParam);

}
