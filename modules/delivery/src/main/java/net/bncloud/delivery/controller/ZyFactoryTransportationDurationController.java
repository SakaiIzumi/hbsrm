package net.bncloud.delivery.controller;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.delivery.param.FactoryTransportationDurationParam;
import net.bncloud.delivery.service.FactoryTransportationDurationService;
import net.bncloud.delivery.vo.FactoryTransportationDurationVo;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author ddh
 * @description 销售工作台-运输时长
 * @since 2022/5/19
 */
@Slf4j
@RestController
@RequestMapping("/zy/transportation-duration")
public class ZyFactoryTransportationDurationController {

    private final FactoryTransportationDurationService transportationDurationService;
    public ZyFactoryTransportationDurationController(FactoryTransportationDurationService transportationDurationService) {
        this.transportationDurationService = transportationDurationService;
    }

    /**
     * 分页查询
     * @param pageable
     * @param queryParam
     * @return
     */
    @PostMapping("/page")
    public R<PageImpl<FactoryTransportationDurationVo>> page(Pageable pageable, @RequestBody QueryParam<FactoryTransportationDurationParam> queryParam) {
        @Valid FactoryTransportationDurationParam param = queryParam.getParam();
        if (param!=null){
            SecurityUtils.getCurrentSupplier().ifPresent(supplier -> {
                param.setSupplierCode(supplier.getSupplierCode());
            });
        }
        return R.data(transportationDurationService.selectPageList(PageUtils.toPage(pageable),queryParam));
    }

}
