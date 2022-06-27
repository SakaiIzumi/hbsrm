package net.bncloud.delivery.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;
import net.bncloud.delivery.mapper.DeliveryPlanDetailItemMapper;
import net.bncloud.delivery.param.DeliveryPlanDetailItemParam;
import net.bncloud.delivery.service.DeliveryPlanDetailItemService;
import net.bncloud.delivery.vo.ShippableDeliveryPlanDetailItemVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 送货计划明细批次表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
@Service
public class DeliveryPlanDetailItemServiceImpl extends BaseServiceImpl<DeliveryPlanDetailItemMapper, DeliveryPlanDetailItem> implements DeliveryPlanDetailItemService {

    @Resource
    private DeliveryPlanDetailItemMapper deliveryPlanDetailItemMapper;

    @Override
    public IPage<DeliveryPlanDetailItem> selectPage(IPage<DeliveryPlanDetailItem> page, QueryParam<DeliveryPlanDetailItemParam> queryParam) {
        return page.setRecords(baseMapper.selectListPage(page, queryParam));
    }

    @Override
    public DeliveryPlanDetailItem queryOneBySourceId(@Param("sourceId") String sourceId) {
        return baseMapper.queryOneBySourceId(sourceId);
    }

    /**
     * 创建和更新送货单状态的时候同时把状态保存到送货计划表中
     *
     * @param deliveryStatusCode
     */
    @Override
    public void updatePlanDeliveryStatus(List<Integer> collect,String deliveryStatusCode) {
        deliveryPlanDetailItemMapper.updatePlanDeliveryStatus(collect,deliveryStatusCode);
    }

    @Override
    public List<ShippableDeliveryPlanDetailItemVo> getShippableDeliveryPlanDetailItemList(String productCode, String orderType, String materialType, String warehouse, String supplierCode, String customerCode) {
        return deliveryPlanDetailItemMapper.getShippableDeliveryPlanDetailItemList(productCode, orderType, materialType, warehouse, supplierCode, customerCode);
    }

    @Override
    public Set<String> getAllWarehouse() {
        return deliveryPlanDetailItemMapper.getAllWarehouse();
    }

    @Override
    public void incrRemainingQuantity(Long deliveryPlanDetailItemId, BigDecimal realDeliveryQuantity) {
        int updateCount = this.baseMapper.incrRemainingQuantity(deliveryPlanDetailItemId, realDeliveryQuantity);
    }


}
