package net.bncloud.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;
import net.bncloud.delivery.param.DeliveryPlanDetailItemParam;
import net.bncloud.delivery.vo.ShippableDeliveryPlanDetailItemVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 送货计划明细批次表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
public interface DeliveryPlanDetailItemMapper extends BaseMapper<DeliveryPlanDetailItem> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-01-17
     */
    List<DeliveryPlanDetailItem> selectListPage(IPage page, QueryParam<DeliveryPlanDetailItemParam> queryParam);

    /**
	 * 送货计划项次
     * @param sourceId 来源系统ID
     * @return 送货计划项次
     */
    DeliveryPlanDetailItem queryOneBySourceId(String sourceId);

    /**
     * 送货计划送货状态更新
     *@param deliveryStatusCode
     *
     */
    void updatePlanDeliveryStatus(@Param("list") List<Integer> collect, @Param("deliveryStatusCode") String deliveryStatusCode);

    /**
     * 查询可发货列表
     * @param deliveryPlanDetailIdSet
     * @param productCodeSet
     */
    List<ShippableDeliveryPlanDetailItemVo> queryShippableList(@Param("deliveryPlanDetailIdSet") Set<Long> deliveryPlanDetailIdSet, @Param("productCodeSet") Set<String> productCodeSet);

    /**
     *
     * @param productCode
     * @param orderType
     * @param materialType
     * @param warehouse
     * @param supplierCode
     * @param customerCode
     */
    List<ShippableDeliveryPlanDetailItemVo> getShippableDeliveryPlanDetailItemList(@Param("productCode") String productCode, @Param("orderType") String orderType, @Param("materialType") String materialType, @Param("warehouse") String warehouse, @Param("supplierCode") String supplierCode, @Param("customerCode") String customerCode);

    /**
     * 查询所有入库仓编码
     */
    Set<String> getAllWarehouse();

    @Update("update t_delivery_plan_detail_item set remaining_quantity = remaining_quantity + #{realDeliveryQuantity} where id = #{deliveryPlanDetailItemId}")
    int incrRemainingQuantity(@Param("deliveryPlanDetailItemId") Long deliveryPlanDetailItemId, @Param("realDeliveryQuantity") BigDecimal realDeliveryQuantity);
}
