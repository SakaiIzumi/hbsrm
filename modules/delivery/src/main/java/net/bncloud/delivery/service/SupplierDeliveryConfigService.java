package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.bncloud.common.api.R;
import net.bncloud.delivery.entity.SupplierDeliveryConfig;
import net.bncloud.delivery.vo.SupplierDeliveryConfigVo;

import java.util.List;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/06/20
 **/
public interface SupplierDeliveryConfigService extends IService<SupplierDeliveryConfig> {
    /**
     * 获取供应商送货配置列表
     * @return
     */
    List<SupplierDeliveryConfigVo> getSupplierDeliveryConfigList();
}
