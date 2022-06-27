package net.bncloud.delivery.controller;

import net.bncloud.common.api.R;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.entity.SupplierDeliveryConfig;
import net.bncloud.delivery.service.SupplierDeliveryConfigService;
import net.bncloud.delivery.vo.SupplierDeliveryConfigVo;
import net.bncloud.service.api.delivery.feign.SupplierDeliveryConfigFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * desc: 供应商送货配置
 *
 * @author Rao
 * @Date 2022/06/20
 **/
@RestController
@RequestMapping("supplierDeliveryConfig")
public class SupplierDeliveryConfigController implements SupplierDeliveryConfigFeignClient {

    @Autowired
    private SupplierDeliveryConfigService supplierDeliveryConfigService;

    /**
     * 获取
     * @return
     */
    @GetMapping("list")
    public R<List<SupplierDeliveryConfigVo>> getSupplierDeliveryConfigList(){

        return R.data(supplierDeliveryConfigService.getSupplierDeliveryConfigList());
    }

    /**
     * 保存或更新 供应商送货配置
     * @return
     */
    @PostMapping("saveOrUpdate")
    public R<Object> saveOrUpdateSupplierDeliveryConfig(@RequestBody SupplierDeliveryConfigVo supplierDeliveryConfigVo){

        Asserts.isTrue(supplierDeliveryConfigService.saveOrUpdate( supplierDeliveryConfigVo ),"配置设置失败，请联系管理员！");
        return R.success();
    }


    @PostMapping("/getSupplierDeliveryConfigList")
    @Override
    public R<List<net.bncloud.service.api.delivery.vo.SupplierDeliveryConfigVo>> getSupplierDeliveryConfigListBySupplierCodeList(List<String> supplierCodeList) {
        List<SupplierDeliveryConfig> supplierDeliveryConfigs = supplierDeliveryConfigService.lambdaQuery().in(SupplierDeliveryConfig::getSupplierCode, supplierCodeList).list();
        return R.data(BeanUtil.copy( supplierDeliveryConfigs,net.bncloud.service.api.delivery.vo.SupplierDeliveryConfigVo.class ));
    }
}
