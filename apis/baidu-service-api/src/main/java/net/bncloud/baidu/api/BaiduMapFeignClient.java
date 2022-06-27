package net.bncloud.baidu.api;

import net.bncloud.baidu.configuration.BaiduMapFeignClientsConfiguration;
import net.bncloud.baidu.fallbackfactory.BaiduMapFeignClientFallbackFactory;
import net.bncloud.baidu.interceptor.BaiduMapRequestInterceptor;
import net.bncloud.baidu.model.dto.BaiduMapGeoCodingDto;
import net.bncloud.baidu.model.dto.BaiduMapRegionDto;
import net.bncloud.baidu.model.vo.BaiduMapGeoCodingResult;
import net.bncloud.baidu.model.vo.BaiduMapRegionResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * desc: 百度地图接口
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@FeignClient(name = "baiduMapFeignClient",url = "${application.baidu-map.url:https://api.map.baidu.com}",configuration = {BaiduMapFeignClientsConfiguration.class,BaiduMapRequestInterceptor.class},fallbackFactory = BaiduMapFeignClientFallbackFactory.class)
public interface BaiduMapFeignClient {

    /**
     * 获取地址经纬度信息
     * @return
     */
    @GetMapping(path = "/geocoding/v3/")
    BaiduMapGeoCodingResult getGeoCoding(@SpringQueryMap BaiduMapGeoCodingDto baiduMapGeoCodingDto );

    /**
     * 获取行政区划分查询
     */
    @GetMapping(path = "/api_region_search/v1/")
    BaiduMapRegionResult getRegion(@SpringQueryMap BaiduMapRegionDto baiduMapRegionDto);

}
