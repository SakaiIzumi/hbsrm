package net.bncloud.delivery.controller;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.baidu.api.BaiduMapFeignClient;
import net.bncloud.baidu.model.dto.BaiduMapRegionDto;
import net.bncloud.baidu.model.vo.BaiduMapRegionResult;
import net.bncloud.baidu.util.BaiduMapResultUtils;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.delivery.entity.DataConfig;
import net.bncloud.delivery.entity.FactoryInfo;
import net.bncloud.delivery.param.FactoryInfoParam;
import net.bncloud.delivery.param.OverAllAdjustParam;
import net.bncloud.delivery.param.OverallsituationConfigChangeParam;
import net.bncloud.delivery.service.DataConfigService;
import net.bncloud.delivery.service.FactoryInfoService;
import net.bncloud.delivery.vo.FactoryInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购工作台-工厂管理
 */
@Slf4j
@RestController
@RequestMapping("/zc/factory-info")
public class ZcFactoryInfoController {


    @Autowired
    private FactoryInfoService factoryInfoService;
    @Resource
    private DataConfigService dataConfigService;
    @Resource
    private BaiduMapFeignClient baiduMapFeignClient;

    /**
     * 分页查询收货工程或供应商工厂
     *
     * @param pageable   page=1&size=10
     * @param queryParam
     * @return
     */
    @PostMapping("/page")
    public R<PageImpl<FactoryInfoVo>> getReceiptPlantPage(Pageable pageable, @RequestBody QueryParam<FactoryInfoParam> queryParam) {
        return R.data(factoryInfoService.getPageList(PageUtils.toPage(pageable), queryParam));
    }

    /**
     * 删除(采购和销售共用)
     *
     * @param ids 工厂信息的id
     * @return
     */
    @PostMapping("/batchDelete")
    public R<String> batchDeletePlant(@RequestBody List<Long> ids) {
        try {
            factoryInfoService.batchDelete(ids);
        } catch (Exception e) {
            log.error("error", e);
            return R.fail(e.toString());
        }
        return R.success("操作成功");
    }

    /**
     * 新增和编辑(采购和销售共用)
     *
     * @param factoryInfos
     * @return
     */
    @PostMapping("/batchUpdate")
    public R<String> batchUpdatePlant(@RequestBody List<FactoryInfo> factoryInfos) {
        try {
            factoryInfoService.batchUpdate(factoryInfos);
        } catch (Exception e) {
            log.error("error", e);
            return R.fail(e.getMessage());
        }
        return R.success("操作成功");
    }


    /**
     * 区域选择器(采购和销售共用)
     *
     * @return
     */
    @GetMapping("/areaSelector")
    public R<List<BaiduMapRegionResult.District>> getAreaSelector() {
        String regionStr = dataConfigService.getOne(Wrappers.<DataConfig>lambdaQuery().eq(DataConfig::getCode, "region")).getValue();
        Asserts.isTrue(StrUtil.isNotBlank(regionStr), "区域信息配置错误，请联系开人员。");
        Asserts.isTrue(regionStr.startsWith("["), "区域信息配置错误，请联系开人员。");
        return R.data(JSON.parseArray(regionStr, BaiduMapRegionResult.District.class));

    }


    /**
     * 重置区域信息
     * @return
     */
    @GetMapping("/resetRegion")
    public R<String> resetRegion(){
        BaiduMapRegionResult baiduMapRegionResult = baiduMapFeignClient.getRegion(BaiduMapRegionDto.builder().sub_admin(4).build());
        BaiduMapResultUtils.successfulResult(baiduMapRegionResult);
        List<BaiduMapRegionResult.District> districts = baiduMapRegionResult.getDistricts();
        dataConfigService.update(Wrappers.<DataConfig>lambdaUpdate()
                .set(DataConfig::getValue,JSON.toJSONString(districts))
                .eq(DataConfig::getCode,"region"));
        return R.success("操作成功");

    }




    /**
     * 工厂新增的时候选择工厂的过滤接口
     *
     */
    @PostMapping("/selectForCreate/{code}")
    @ApiOperation(value = "工厂新增的时候选择工厂的过滤接口", notes = "所属的供应方/采购方编码")
    public R<List<FactoryInfoVo>> selectForCreate(@PathVariable("code") String code){
        List<FactoryInfoVo> factoryInfoList=factoryInfoService.selectForCreate(code);
        return R.data(factoryInfoList);
    }

    /**
     * 修改默认全局工作日的配置 (此配置只会对采购生效)
     *
     * 把使用默认配置的采购的工厂的默认假期进行修改
     *
     */
    @DeleteMapping("/defaultConfigChange")
    @ApiOperation(value = "修改默认全局工作日的配置", notes = "")
    public R overallsituationConfigChange(@RequestBody OverallsituationConfigChangeParam param){
        factoryInfoService.overallsituationConfigChange(param);
        return R.success();
    }

    /**
     * 采购方全局默认工作日配置调整接口
     *
     * 全局配置调整后给没有设置自定义默认工作日的采购工厂主数据使用的
     */
    @PostMapping("/overAllAdjustment")
    @ApiOperation(value = "购方全局默认工作日配置调整接口", notes = "传入ids")
    public R overAllAdjustment(@RequestBody OverAllAdjustParam param){
        factoryInfoService.overAllAdjustment(param);
        return R.success();
    }

}
