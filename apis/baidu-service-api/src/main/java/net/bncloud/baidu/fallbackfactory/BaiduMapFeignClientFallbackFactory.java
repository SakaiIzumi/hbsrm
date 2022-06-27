package net.bncloud.baidu.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.baidu.api.BaiduMapFeignClient;
import net.bncloud.baidu.model.dto.BaiduMapGeoCodingDto;
import net.bncloud.baidu.model.dto.BaiduMapRegionDto;
import net.bncloud.baidu.model.vo.BaiduMapGeoCodingResult;
import net.bncloud.baidu.model.vo.BaiduMapRegionResult;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@Slf4j
public class BaiduMapFeignClientFallbackFactory implements FallbackFactory<BaiduMapFeignClient> {
    @Override
    public BaiduMapFeignClient create(Throwable throwable) {
        log.error("[BaiduMapFeignClientFallbackFactory] 请求百度地图api接口错误！",throwable);
        return new BaiduMapFeignClient(){

            @Override
            public BaiduMapGeoCodingResult getGeoCoding(BaiduMapGeoCodingDto baiduMapGeoCodingDto) {
                return new BaiduMapGeoCodingResult();
            }

            @Override
            public BaiduMapRegionResult getRegion(BaiduMapRegionDto baiduMapRegionDto) {
                return new BaiduMapRegionResult();
            }
        };
    }
}
