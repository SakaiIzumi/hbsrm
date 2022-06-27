package net.bncloud.delivery.service;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.entity.DeliveryNoteExcelImportDetailVo;
import net.bncloud.delivery.vo.DeliveryDetailVo;
import net.bncloud.delivery.param.DeliveryDetailParam;
import net.bncloud.base.BaseService;
import net.bncloud.delivery.vo.PrintDataVo;
import net.bncloud.delivery.vo.ShippableDeliveryPlanDetailItemVo;
import net.bncloud.serivce.api.order.dto.OrderDetailDTO;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 送货明细表 服务类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
public interface DeliveryDetailService extends BaseService<DeliveryDetail> {


    /**
     * 保存送货明细
     * @param deliveryDetailList 送货明细
     * @return
     */
    List<DeliveryDetailVo> saveDetails(Long deliveryId,List<DeliveryDetailVo> deliveryDetailList);

    List<DeliveryDetail> getDeliveryDetailList(Pageable pageable, QueryParam<DeliveryDetailParam> queryParam);

    PrintDataVo<DeliveryDetailVo> printData(QueryParam<DeliveryDetailParam> queryParam);

    void updateDetailErpId(List<DeliveryDetail> deliveryDetailList);

    DeliveryDetail getDetailById(Long id );

    /**
     * 匹配导入的Excel数据
     * @param deliveryNoteExcelImportDetailVoList
     * @param deliveryNote
     * @param productCodeShippableDeliveryPlanDetailItemListMap
     * @param ratioOfSentProportion
     */
    void matchImportExcelData(List<DeliveryNoteExcelImportDetailVo> deliveryNoteExcelImportDetailVoList, DeliveryNote deliveryNote, Map<String, List<ShippableDeliveryPlanDetailItemVo>> productCodeShippableDeliveryPlanDetailItemListMap, BigDecimal ratioOfSentProportion);

    OrderDetailDTO getMrpDetailById(Long deliveryPlanDetailItemId);

    /**
     * 查询物料在途数量
     * @param supplierCode
     * @param purchaseCode
     * @param materialCodeSet
     * @return
     */
    List<DeliveryDetailVo> queryInTransitQuantity(String supplierCode, String purchaseCode, Set<String> materialCodeSet);
}
