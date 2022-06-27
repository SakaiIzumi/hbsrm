package net.bncloud.delivery.controller;


import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.delivery.entity.FactoryInfo;
import net.bncloud.delivery.enums.FactoryBelongTypeEnum;
import net.bncloud.delivery.param.FactoryInfoParam;
import net.bncloud.delivery.service.FactoryInfoService;
import net.bncloud.delivery.vo.FactoryInfoVo;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 销售工作台-工厂管理
 */
@RestController
@RequestMapping("/zy/factory-info")
public class ZyFactoryInfoController {

    private final FactoryInfoService factoryInfoService;
    public ZyFactoryInfoController(FactoryInfoService factoryInfoService) {
        this.factoryInfoService = factoryInfoService;
    }

    /**
     * 分页查询供应商工厂
     * @param pageable
     * @param queryParam
     * @return
     */
    @PostMapping("/page")
    public R<PageImpl<FactoryInfoVo>> page(Pageable pageable, @RequestBody QueryParam<FactoryInfoParam> queryParam) {
        @Valid FactoryInfoParam param = queryParam.getParam();
        if (param != null && FactoryBelongTypeEnum.SUPPLIER.getCode().equals(param.getBelongType())) {
            SecurityUtils.getCurrentSupplier().ifPresent(supplier -> {
                param.setBelongCode(supplier.getSupplierCode());
            });
        }
        return R.data(factoryInfoService.getPageList(PageUtils.toPage(pageable), queryParam));
    }


}
