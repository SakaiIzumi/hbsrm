package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;
import net.bncloud.delivery.param.DeliveryPlanDetailItemParam;
import net.bncloud.delivery.vo.ShippableDeliveryPlanDetailItemVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 送货计划明细批次表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
public interface DeliveryPlanDetailItemService extends BaseService<DeliveryPlanDetailItem> {

    /**
     * 自定义分页
     *
     * @param page
     * @param queryParam
     * @return
     */
    IPage<DeliveryPlanDetailItem> selectPage(IPage<DeliveryPlanDetailItem> page, QueryParam<DeliveryPlanDetailItemParam> queryParam);


	/**
	 * 查询送货计划明细项次信息
	 * @param sourceId 来源系统ID
	 * @return 计划明细项次
	 */
	DeliveryPlanDetailItem queryOneBySourceId(String sourceId);

	/**
	 * 更新送货单的送货状态
	 *
	 */
	void updatePlanDeliveryStatus(List<Integer> collect ,String deliveryStatusCode);

	/**
	 * 获取可发货的送货计划项次数据
	 * @param productCode
	 * @param orderType
	 * @param materialType
	 * @param warehouse
	 * @param supplierCode
	 * @param customerCode
	 * @return
	 */
    List<ShippableDeliveryPlanDetailItemVo> getShippableDeliveryPlanDetailItemList(String productCode, String orderType, String materialType, String warehouse, String supplierCode, String customerCode);

	/**
	 * 获取所有入库仓
	 * @return
	 */
	Set<String> getAllWarehouse();

	/**
	 * 原子新增剩余可发货数量
	 * @param deliveryPlanDetailItemId
	 * @param realDeliveryQuantity
	 */
	void incrRemainingQuantity(Long deliveryPlanDetailItemId, BigDecimal realDeliveryQuantity);
}
