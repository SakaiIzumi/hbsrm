package net.bncloud.oem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.oem.domain.entity.FileInfo;
import net.bncloud.oem.domain.entity.PurchaseOrder;
import net.bncloud.oem.domain.param.PurchaseOrderParam;
import net.bncloud.oem.domain.param.PurchaseOrderReceivingParam;
import net.bncloud.oem.domain.vo.PurchaseOrderVo;
import net.bncloud.oem.domain.vo.ReceivingRecordsVo;
import net.bncloud.oem.domain.vo.ReturnedReceiptsVo;
import net.bncloud.oem.domain.vo.ToBeConfirmVo;
import net.bncloud.oem.service.api.vo.OemPurchaseOrderVo;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ddh
 * @description
 * @since 2022/4/24
 */
public interface PurchaseOrderService extends BaseService<PurchaseOrder> {

    /**
     * 同步甲供物料订单
     * @param oemPurchaseOrderVoList
     */
    void syncOemPurchaseOrder(List<OemPurchaseOrderVo> oemPurchaseOrderVoList);

    /**
     * 分页查询采购订单
     *
     * 采购端和供应商共用的分页方法,采购端需要查询三层,供应商两层
     *
     * 两端查询的数据不影响,第三层供应商端不显示就可以了
     * @param page
     * @param queryParam
     * @return
     */
    PageImpl<PurchaseOrderVo> selectList(IPage<PurchaseOrderVo> page, QueryParam<PurchaseOrderParam> queryParam);


    PageImpl<PurchaseOrderVo> selectOemSupplierPage(IPage<PurchaseOrderVo> page, QueryParam<PurchaseOrderParam> queryParam);

    /**
     * 分页待确认查询
     *
     * @param page
     * @param queryParam
     * @return
     */
    PageImpl<ToBeConfirmVo> selectToBeConfirmList(IPage<ToBeConfirmVo> page, QueryParam<PurchaseOrderParam> queryParam);

    /**
     * 根据id查询采购订单详情
     * @param id
     * @return
     */
    PurchaseOrderVo getOrderInfo(Long id);

    List<ReceivingRecordsVo> getReceivingByOrderId(Long id);

    /**
     * 批量删除收货
     * @param ids
     */
    void batchDeleteReceiving(List<Long> ids);

    void batchSaveReceiving(List<PurchaseOrderReceivingParam> params);

    /**
     * 通过源ErpId
     * @return
     */
    PurchaseOrder getBySourceErpId(String sourceErpId);


    Map<String,Long> getReceivingAddressExcelAttachmentId();

    FileInfo importReceivingAddress(MultipartFile file) throws IOException;

    /**
     * 被退回
     * @param page
     * @param queryParam
     * @return
     */
    PageImpl<ReturnedReceiptsVo> getReturnedReceiptsPage(IPage<ReturnedReceiptsVo> page, QueryParam<PurchaseOrderParam> queryParam);

    /**
     * 订单收货
     * @param id 采购订单id
     * @return
     */
    PurchaseOrderVo getOrderReceipt(Long id);

    List<ReturnedReceiptsVo> batchEditPageList(List<Long> ids);

}
