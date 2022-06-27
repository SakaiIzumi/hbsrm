# 使用教程
```java
        BaiduMapGeoCodingResult baiduMapGeoCodingResult = baiduMapFeignClient.getGeoCoding(BaiduMapGeoCodingDto.builder().address("水口").build());
        BaiduMapResultUtils.successfulResult( baiduMapGeoCodingResult );

        BaiduMapRegionResult baiduMapRegionResult = baiduMapFeignClient.getRegion(BaiduMapRegionDto.builder().keyword("番禺区").sub_admin(2).build());
        BaiduMapResultUtils.successfulResult( baiduMapRegionResult );
```