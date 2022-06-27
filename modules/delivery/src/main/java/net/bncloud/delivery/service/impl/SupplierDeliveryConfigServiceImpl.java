package net.bncloud.delivery.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.security.Supplier;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.entity.SupplierDeliveryConfig;
import net.bncloud.service.api.delivery.enums.SupplierDeliveryConfigCode;
import net.bncloud.delivery.mapper.SupplierDeliveryConfigMapper;
import net.bncloud.delivery.service.SupplierDeliveryConfigService;
import net.bncloud.delivery.vo.SupplierDeliveryConfigVo;
import net.bncloud.service.api.platform.config.ConfigParamOpenFeign;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/06/20
 **/
@Slf4j
@Service
public class SupplierDeliveryConfigServiceImpl extends BaseServiceImpl<SupplierDeliveryConfigMapper, SupplierDeliveryConfig> implements SupplierDeliveryConfigService {

    @Override
    public List<SupplierDeliveryConfigVo> getSupplierDeliveryConfigList() {

        Optional<Supplier> currentSupplierOpt = SecurityUtils.getCurrentSupplier();
        if(! currentSupplierOpt.isPresent()) {
            return new ArrayList<>();
        }
        Supplier supplier = currentSupplierOpt.get();

        List<SupplierDeliveryConfig> supplierDeliveryConfigs = this.lambdaQuery().eq(SupplierDeliveryConfig::getSupplierCode, supplier.getSupplierCode() ).list();
        List<SupplierDeliveryConfigVo> supplierDeliveryConfigVoList = BeanUtil.copy(supplierDeliveryConfigs, SupplierDeliveryConfigVo.class);

        Map<String, String> codeValueMap = supplierDeliveryConfigVoList.stream().collect(Collectors.toMap(SupplierDeliveryConfig::getCode, SupplierDeliveryConfig::getValue));

        List<SupplierDeliveryConfigVo>  supplierDeliveryConfigList =  new ArrayList<> (supplierDeliveryConfigVoList);

        // 感觉这里设计成过滤器很ok  赶时间不想写
        String defaultWeekdayEnableCode = SupplierDeliveryConfigCode.defaultWeekdayEnable.name();
        if(! codeValueMap.containsKey(defaultWeekdayEnableCode )  ){
            SupplierDeliveryConfigVo supplierDeliveryConfigVo = new SupplierDeliveryConfigVo();
            supplierDeliveryConfigVo.setSupplierCode( supplier.getSupplierCode() );
            supplierDeliveryConfigVo.setCode(defaultWeekdayEnableCode);
            supplierDeliveryConfigVo.setValue( SupplierDeliveryConfigCode.defaultWeekdayEnable.getDefaultValue() );
            supplierDeliveryConfigVo.setRemark( SupplierDeliveryConfigCode.defaultWeekdayEnable.getDesc() );
            supplierDeliveryConfigList.add( supplierDeliveryConfigVo );
        }

        String autoImportLegalHolidaysEnableCode = SupplierDeliveryConfigCode.autoImportLegalHolidaysEnable.name();
        if(! codeValueMap.containsKey( autoImportLegalHolidaysEnableCode ) ){
            SupplierDeliveryConfigVo supplierDeliveryConfigVo = new SupplierDeliveryConfigVo();
            supplierDeliveryConfigVo.setSupplierCode( supplier.getSupplierCode() );
            supplierDeliveryConfigVo.setCode(autoImportLegalHolidaysEnableCode);
            supplierDeliveryConfigVo.setValue( SupplierDeliveryConfigCode.autoImportLegalHolidaysEnable.getDefaultValue() );
            supplierDeliveryConfigVo.setRemark( SupplierDeliveryConfigCode.autoImportLegalHolidaysEnable.getDesc() );
            supplierDeliveryConfigList.add( supplierDeliveryConfigVo );
        }
        return supplierDeliveryConfigList;

    }
}
