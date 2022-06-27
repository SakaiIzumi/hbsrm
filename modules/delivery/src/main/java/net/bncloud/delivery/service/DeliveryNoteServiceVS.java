package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.entity.DeliveryNoteExcelModel;
import net.bncloud.delivery.param.DeliveryDetailBatchDetailParams;
import net.bncloud.delivery.param.DeliveryNoteImportDetailParam;
import net.bncloud.delivery.param.DeliveryNoteParam;
import net.bncloud.delivery.param.DeliveryNoteSaveParam;
import net.bncloud.delivery.vo.*;
import net.bncloud.service.api.delivery.dto.DeliveryDetailUpdateDTO;
import net.bncloud.service.api.delivery.dto.DeliveryMaterialNoticeDTO;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 送货单信息表 服务类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */

public interface DeliveryNoteServiceVS extends BaseService<DeliveryNote> {

    /**
     * 删除送货单
     */
    boolean deleteDeliveryNote(Long id);

    /**
     * 自定义分页
     * @param page
     * @param queryParam
     * @return
     */
    IPage<DeliveryNote> selectPage(IPage<DeliveryNote> page, QueryParam<DeliveryNoteParam> queryParam);

    IPage<DeliveryNote> selectPageByOrgId(IPage<DeliveryNote> page, QueryParam<DeliveryNoteParam> queryParam);

    /**
     * 保存送货单信息
     * @param deliveryNote
     * @return
     */
    DeliveryNoteVo saveDeliveryNote(DeliveryNoteSaveParam deliveryNote);

    /**
     * 更新送货单信息
     * @param deliveryNote
     * @return
     */
    DeliveryNoteVo updateDeliveryNote(DeliveryNoteSaveParam deliveryNote);

    /**
     * 更新送货单信息（集成update和updateStatus的方法）
     * @param deliveryNote
     * @return
     */
    DeliveryNoteVo updateSign(DeliveryNoteSaveParam deliveryNote);


    /**
     * 更新送货单的收料通知单erpId
     * @param erpId
     * @return
     */
    void updateErpId(Long id ,Long erpId,String fNumber,String code);

    /**
     * 送货单数量统计
     * @return
     */
    DeliveryStatisticsVo statistics();

    /**
     * 送货单数量统计
     * @return
     */
    DeliveryStatisticsVo selectToBeSignCount();
    /**
     * 全部的数量统计
     * @return
     */
    DeliveryStatisticsVo selectStatistics();

    /**
     * 获取送货通知单详情
     * @param id
     * @return
     */
    DeliveryNoteVo getDeliveryNoteInfo(Long id);

    /**
     * 申请发货
     */
    R applyDelivery(Long id);

    /**
     * 作废申请
     */
    R invalidApply(Long id);

    /**
     * 撤回申请
     */
    R withdrawApply(Long id);

    /**
     * 确认发出
     */
    R confirmationIssued(Long id);

    /**
     * 撤回送货
     */
    R withdrawDelivery(Long id);

    /**
     * 货物已发
     */
    R delivered(Long id);

    /**
     * 作废送货
     */
    R invalidDelivery(Long id);

    /**
     * 提醒
     */
    R remind(Long id);

    /**
     * 设置附件
     * @param record
     */
    void buildAttachment(DeliveryNoteVo record);

    /**
     * 批量设置附件
     * @param records
     */
    void buildAttachmentBatch(List<DeliveryNoteVo> records);

    /**
     * 设置权限按钮
     * @param records
     */
    void buildPermissionButtonBatch(List<DeliveryNoteVo> records);

    /**
     * 列表查询
     * @param params
     * @return
     */
    List<DeliveryNote> queryList(String params);

    /**
     * 根据收、发货单ID 更新单据同步结算池状态
     * @param deliveryIdList 收、发货单ID列表
     * @return
     */
    boolean updateSettlementPoolSyncStatus(List<Long> deliveryIdList);

    /**
     * 根据收、发货单ID，查询收发货单信息列表
     * @param deliveryIdList 收、发货单ID
     * @return
     */
    List<DeliveryNote> queryListByDeliveryIds(List<Long> deliveryIdList);

    /**
     * 物料通知单
     * @param deliveryId 送货通知 ID
     * @return 物料通知单信息
     */
    DeliveryMaterialNoticeDTO wrapMaterialNotice(Long deliveryId);

    /**
     * 同步ERP送货通知单信息
     * @param deliveryNoteUpdateDTO ERP送货通知单信息
     */
    void syncErpDeliveryNoteDetailInfo(DeliveryDetailUpdateDTO deliveryNoteUpdateDTO);

    Integer selectCount();

    /**
     * 批量发货
     * @param ids
     */
    Long batchDelivery(List<String> ids);



    /**
     * qu'ren确认发货
     * @param id
     */
    void updateStatus(Long id);

    /**
     * 确认发布（把送货单状态改成待签收的状态）
     * @param deliveryNote
     */
    DeliveryNoteVo saveAndSign(DeliveryNoteSaveParam deliveryNote);





   void updateDeliveryNoteV2(Long id );


    /**
     * 设置送货明细附件
     * @param vo
     */
    void setDeliveryDetailAttachment(DeliveryDetailVo vo);

    /**
     * 批量设置送货明细附件
     * @param records
     */
    void batchSetDeliveryDetailAttachment(List<DeliveryDetailVo> records);

    /**
     * 导出，不分页查询所有数据
     * @return
     */
    List<DeliveryNoteExcelModel> getDeliveryNoteList(QueryParam<DeliveryNoteParam> queryParam);

    /**
     * 导入明细
     * @param file
     * @param deliveryNoteImportDetailParam
     * @return
     */
    DeliveryDetailExcelImportResult importDetail(MultipartFile file, DeliveryNoteImportDetailParam deliveryNoteImportDetailParam);

    /**
     * 获取导入送货明细状态
     * @param deliveryNoteId
     * @return
     */
    DeliveryDetailExcelImportResult getImportDetailStatus(Long deliveryNoteId);

    /**
     * 获取导入明细excel模板下载地址
     * @return
     */
    Map<String,Long> getImportDetailExcelAttachmentId();

    /**
     * 获取分批详情
     * @param deliveryDetailBatchDetailParams
     * @return
     */
    DeliveryDetailBatchDetailVo getDeliveryDetailBatchDetail(DeliveryDetailBatchDetailParams deliveryDetailBatchDetailParams);

    /**
     * 保存送货明细 --> 批次明细保存 （包含送货单保存逻辑）
     * @param deliveryNoteSaveParam
     * @return
     */
    DeliveryNoteVo saveDeliveryDetailBatchDetailSave(DeliveryNoteSaveParam deliveryNoteSaveParam);

    /**
     * 获取组织信息
     * @param supplierCode
     * @param customerCode
     * @return
     */
    public OrgIdDTO getOrgIdDto(String supplierCode, String customerCode);

    /**
     * 获取组织ID
     * @param supplierCode
     * @param customerCode
     * @return
     */
    public Long remoteGetOrgId(String supplierCode, String customerCode);

    /**
     * 保存mrp送货单信息
     * @param deliveryNote
     * @return
     */
    DeliveryNoteVo saveMrpDeliveryNote(DeliveryNoteSaveParam deliveryNote);

    /**
     * mrp确认发布（把mrp送货单状态改成待签收的状态）
     * @param deliveryNote
     */
    DeliveryNoteVo mrpSaveAndSign(DeliveryNoteSaveParam deliveryNote);

    /**
     * 更新mrp送货单信息
     * @param deliveryNote
     * @return
     */
    DeliveryNoteVo updateMrpDeliveryNote(DeliveryNoteSaveParam deliveryNote);

    /**
     * 更新mrp送货单信息（集成update和updateStatus的方法）
     * @param deliveryNote
     * @return
     */
    DeliveryNoteVo mrpUpdateSign(DeliveryNoteSaveParam deliveryNote);

    /**
     * 删除mrp送货单
     */
    boolean deleteMrpDeliveryNote(Long id);

    /**
     * mrp保存送货明细 --> 批次明细保存 （包含送货单保存逻辑）
     * @param deliveryNoteSaveParam
     * @return
     */
    DeliveryNoteVo saveMrpDeliveryDetailBatchDetailSave(DeliveryNoteSaveParam deliveryNoteSaveParam);

    /**
     * 获取mrp分批详情
     * @param deliveryDetailBatchDetailParams
     * @return
     */
    MrpDeliveryOrderDetailVo getMrpDeliveryDetailBatchDetail(DeliveryDetailBatchDetailParams deliveryDetailBatchDetailParams);
}
