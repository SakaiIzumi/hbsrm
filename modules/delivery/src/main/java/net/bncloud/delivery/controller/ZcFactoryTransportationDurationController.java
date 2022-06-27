package net.bncloud.delivery.controller;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.delivery.entity.FactoryInfo;
import net.bncloud.delivery.entity.FactoryTransportationDuration;
import net.bncloud.delivery.param.FactoryInfoParam;
import net.bncloud.delivery.param.FactoryTransportationDurationParam;
import net.bncloud.delivery.service.FactoryTransportationDurationService;
import net.bncloud.delivery.vo.FactoryTransportationDurationVo;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author ddh
 * @description 采购工作台-工厂运输时长
 * @since 2022/5/17
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/zc/transportation-duration")
public class ZcFactoryTransportationDurationController {

    @Resource
    private FactoryTransportationDurationService transportationDurationService;

    /**
     * 供应商送货时长管理
     *
     * @param pageable
     * @param queryParam
     * @return
     */
    @PostMapping("/page")
    public R<PageImpl<FactoryTransportationDurationVo>> page(Pageable pageable, @RequestBody QueryParam<FactoryTransportationDurationParam> queryParam) {
        return R.data(transportationDurationService.selectPageList(PageUtils.toPage(pageable), queryParam));
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/batchDelete")
    public R<String> batchDelete(@RequestBody List<Long> ids) {
        try {
            transportationDurationService.batchDeleteTransportationDuration(ids);
        } catch (Exception e) {
            log.error("error", e);
            return R.fail(e.getMessage());
        }
        return R.success("操作成功");
    }

    /**
     * 新增或编辑（保存）
     *
     * @param factoryTransportationDurationList
     * @return
     */
    @PostMapping("/save")
    public R<String> save(@Valid @RequestBody List<FactoryTransportationDuration> factoryTransportationDurationList) {
        try {
            transportationDurationService.addOrEditTransportationDuration(factoryTransportationDurationList);
        } catch (Exception e) {
            log.error("error", e);
            return R.fail(e.getMessage());
        }
        return R.success("操作成功");
    }

    /**
     * 工厂信息选择器
     *
     * @param param
     * @return
     */
    @PostMapping("/factoryInfoSelector")
    public R<List<FactoryInfo>> factoryInfoSelector(@RequestBody QueryParam<FactoryInfoParam> param) {
        return R.data(transportationDurationService.getFactoryInfoList(param));
    }

}
