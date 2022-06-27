package net.bncloud.oem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.oem.domain.entity.ReceivingAddress;
import net.bncloud.oem.domain.param.ReceivingAddressParam;
import net.bncloud.oem.domain.vo.ReceivingAddressVo;

import java.util.List;
import net.bncloud.oem.service.api.vo.OemReceivingAddressVo;

import java.util.List;
import java.util.Map;

/**
 * @author ddh
 * @description
 * @since 2022/4/24
 */
public interface ReceivingAddressService  extends BaseService<ReceivingAddress> {
    IPage<ReceivingAddressVo> selectPage(IPage<ReceivingAddress> page, QueryParam<ReceivingAddressParam> param);

    void updateSupplier(ReceivingAddressParam param);

    void batchUpdateSupplier(List<ReceivingAddressParam> receivingAddressParamList);

    /**
     * 同步甲供物料订单收货协同地址信息
     * @param oemReceivingAddressVoList
     */
    void syncOemReceivingAddress(List<OemReceivingAddressVo> oemReceivingAddressVoList);

    /**
     * 同步甲供物料订单收货协同地址信息
     */
    Map<String, ReceivingAddress> syncOemAddress(List<String> addressList);

    /**
     *
     * @param code
     * @return
     */
    ReceivingAddress getByCode(String code);
}
