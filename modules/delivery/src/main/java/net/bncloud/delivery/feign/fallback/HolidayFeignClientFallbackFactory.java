package net.bncloud.delivery.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.delivery.feign.HolidayFeignClient;
import net.bncloud.delivery.vo.apihub.ApiHubResult;
import net.bncloud.delivery.vo.apihub.HolidayParam;
import org.springframework.stereotype.Component;

/**
 * desc: 节假日FeignClientFallbackFactory
 *
 * @author Rao
 * @Date 2022/05/19
 **/
@Slf4j
@Component
public class HolidayFeignClientFallbackFactory implements FallbackFactory<HolidayFeignClient> {


    @Override
    public HolidayFeignClient create(Throwable throwable) {
        log.error("查询节假日接口数据失败！",throwable);
        return new HolidayFeignClient() {
            @Override
            public ApiHubResult<ApiHubResult.PageInfo<ApiHubResult.DateInfo>> getHolidays(HolidayParam holidayParam) {
                return new ApiHubResult<>(-1,"查询节假日数据失败！");
            }
        };
    }
}
