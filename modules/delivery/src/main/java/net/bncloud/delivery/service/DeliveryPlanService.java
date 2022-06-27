package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryPlan;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;
import net.bncloud.delivery.param.*;
import net.bncloud.delivery.vo.*;
import net.bncloud.service.api.delivery.dto.DeliveryPlanDTO;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 送货计划基础信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-15
 */
public interface DeliveryPlanService extends BaseService<DeliveryPlan> {

    /**
     * 自定义分页
     *
     * @param page
     * @param QueryParam
     * @return
     */
    PageImpl<DeliveryPlan> selectPage(IPage<DeliveryPlan> page, QueryParam<DeliveryPlanParam> QueryParam, String workBench);

    /**
     * 送货计划数量统计
     *
     * @return
     */
    DeliveryPlanStatisticsVo getStatisticsInfo(String workBench);

    /**
     * 根据id查询送货计划明细
     *
     * @param
     * @return
     */
    DeliveryPlan getDeliveryPlanInfo(String id);


    /**
     * 分页查询送货计划明细
     *
     * @param pageable  page=1&size=10
     * @param pageParam 请求参数
     * @return
     */
    IPage<PlanSchedulingDetailVo> getDeliveryPlanDetailList(Pageable pageable, QueryParam<DeliveryPlanDetailParam> pageParam);


    /**
     * 根据明细id查询送货批次
     *
     * @param deliveryPlanDetailId 送货计划明细id
     * @return
     */
    R<List<DeliveryPlanDetailItem>> getDeliveryPlanDetailItemList(String deliveryPlanDetailId);

    /**
     * 批量发送送货计划
     *
     * @param ids
     */
    void sendBatch(List<String> ids);

    /**
     * 发送送货计划
     *
     * @param id
     */
    void send(Long id);

    /**
     * 提醒供应商确认送货计划
     *
     * @param id
     */
    void remind(Long id);

    /**
     * 供应商确认送货计划
     *
     * @param id
     */
    void confirm(Long id);


    /**
     * 送货计划看板
     *
     * @param dateStr
     * @param queryParam
     * @return
     */
    List<DeliveryPlanBoardVo> getDeliveryPlanBoard(String dateStr, QueryParam<DeliveryPlanDetailItemParam> queryParam);

    /**
     * 接收送货计划
     *
     * @param deliveryPlanDTO 送货计划
     */
    void receiveDeliveryPlan(DeliveryPlanDTO deliveryPlanDTO);

    /**
     * 看板详情
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<DeliveryPlanDetailItemVo> getDeliveryPlanBoardDetail(IPage<DeliveryPlanDetailItemVo> page, QueryParam<DeliveryPlanBoardParam> pageParam);

    /**
     * 打印
     *
     * @param pageParam
     * @return
     */
    PrintDataVo<DeliveryPlanDetailItemVo> printData(QueryParam<DeliveryPlanBoardParam> pageParam);

    /**
     * 按计划送货
     *
     * @param page
     * @param queryParam
     * @return
     */
    IPage<DeliveryPlanDetailItemVo> getDeliveryAsPlanList(IPage<DeliveryPlanDetailItemVo> page, QueryParam<DeliveryAsPlanParam> queryParam);


    void batchSaveOrUpdateItem(List<DeliveryPlanDetailItem> items);

    void batchDeleteItem(List<Long> ids);

    void differenceConfirmById(Long planId);

    void batchUpdateItem(List<DeliveryPlanDetailItem> itemList);

    PageImpl<PlanSchedulingBoardVo> getZcPlanSchedulingBoardPage(IPage<PlanSchedulingBoardVo> page, QueryParam<PlanSchedulingBoardParam> queryParam);

    PageImpl<PlanSchedulingBoardVo> getZyPlanSchedulingBoardPage(IPage<PlanSchedulingBoardVo> page, QueryParam<PlanSchedulingBoardParam> queryParam);

    void remindPlanScheduling(List<Long> planDetailIds);

    void publishPlanScheduling();

    void differenceConfirmPlanScheduling(List<Long> planDetailIds);

    void confirmPlanSchedulingDetail(List<Long> planDetailIds);

    void batchSavePlanSchedulingDetailItem(List<PlanSchedulingDetailItem> itemList);

    void exportZcPlanScheduling(HttpServletResponse response, QueryParam<PlanSchedulingBoardParam> queryParam) throws IOException;

    void exportZyPlanScheduling(HttpServletResponse response, QueryParam<PlanSchedulingBoardParam> queryParam) throws IOException;

    /**
     * 查询上一版 项次送货数量和
     *
     * @param mrpComputerNo
     * @return
     */
    Map<String, BigDecimal> getPreVersionItemDeliveryQuantity(String mrpComputerNo);

    void confirmAllPlanSchedulingDetail();


    Long getOrgIdByPurchaseCodeAndSupplierCode(String purchaseCode, String supplierCode);

    Boolean getMrpAutoSendCfg();


}
