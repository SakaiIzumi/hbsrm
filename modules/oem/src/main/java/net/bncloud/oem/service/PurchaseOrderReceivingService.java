package net.bncloud.oem.service;

import net.bncloud.base.BaseService;
import net.bncloud.oem.domain.entity.PurchaseOrderReceiving;
import net.bncloud.oem.domain.param.ReturnReceivingParam;
import net.bncloud.oem.domain.vo.RemarkVo;

import java.util.List;

/**
 * @author ddh
 * @description 收货信息
 * @since 2022/4/24
 */
public interface PurchaseOrderReceivingService extends BaseService<PurchaseOrderReceiving> {

    /**
     * 统计已退回的数量
     * @return
     */
     Integer statisticsReturnedQuantity();

    /**
     * 统计待确认的数量
     * @return
     */
    Integer statisticsToBeConfirmQuantity();


    /**
     * 退回收货
     * @param param
     */
    void returnReceiving(ReturnReceivingParam param);

    /**
     * 批量确认
     * @param ids
     */
    void batchConfirmReceiving(List<String> ids,List<Long> receiveids);

    void batchConfirmReceivingV2(List<Long> ids);

    /**
     * 批量退回
     * @param ids
     */
    void batchRejectReceiving(List<String> ids);


    /**
     * 收货确认
     *
     */
    void confirmStatus(Long id);

    RemarkVo queryRemark(Long id);


    void toBeConfirmBatchConfirmReceiving(List<String> ids);
}
