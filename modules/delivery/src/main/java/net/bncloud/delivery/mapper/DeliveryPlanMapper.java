package net.bncloud.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryPlan;
import net.bncloud.delivery.entity.DeliveryPlanBoard;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;
import net.bncloud.delivery.param.DeliveryAsPlanParam;
import net.bncloud.delivery.param.DeliveryPlanBoardParam;
import net.bncloud.delivery.param.DeliveryPlanDetailItemParam;
import net.bncloud.delivery.param.DeliveryPlanParam;
import net.bncloud.delivery.vo.DeliveryPlanDetailItemVo;
import net.bncloud.delivery.vo.PreVersionDeliveryPlanDetailItemVo;
import net.bncloud.delivery.vo.ShippableDeliveryPlanDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 送货计划基础信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-15
 */
public interface DeliveryPlanMapper extends BaseMapper<DeliveryPlan> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-01-15
     */
    List<DeliveryPlan> selectListPage(IPage page, @Param("pageParam") QueryParam<DeliveryPlanParam> pageParam,@Param("workBench") String workBench);


    /**
     * 计划看板详情
     *
     * @return
     */
    List<DeliveryPlanDetailItemVo> selectDeliveryPlanBoardDetail(IPage page, @Param("pageParam") QueryParam<DeliveryPlanBoardParam> pageParam);


    /**
     * 送货单-按计划送货
     *
     * @param page
     * @param queryParam
     * @return
     */
    List<DeliveryPlanDetailItemVo> selectDeliveryAsPlan(IPage page, @Param("queryParam") QueryParam<DeliveryAsPlanParam> queryParam);

    /**
     * 根据来源系统主键ID查询记录
     *
     * @param sourceId 来源系统主键ID
     * @return 送货计划
     */
    DeliveryPlan selectOneBySourceId(@Param("sourceId") String sourceId);


    /**
     * 计划看板
     *
     * @param date
     * @param queryParam
     * @return
     */
    List<DeliveryPlanBoard> selectPlanBoard(@Param("date") String date, @Param("queryParam") QueryParam<DeliveryPlanDetailItemParam> queryParam);


    /**
     * 根据计划明细项次id查询看板详情
     *
     * @param id
     * @return
     */
    DeliveryPlanDetailItemVo getPlanBoardDetailById(@Param("id") String id);

    /**
     * 根据计划id查询计划明细项次
     *
     * @param planId
     * @return
     */
    List<DeliveryPlanDetailItem> getPlanDetailItemByPlanId(@Param("planId") Long planId);


    /**
     * （关联查询送货单的信息） 项次列表
     * @param id
     * @return
     */
    List<DeliveryPlanDetailItem> getItemByDetailId(@Param("id") String id);


    /**
     * 查询可发货的送货计划明细项次 with 供应商code和采购方code
     * @param supplierCode
     * @param customerCode
     */
    List<ShippableDeliveryPlanDetailVo> queryDeliveryPlanDetailListWithShippableBySupplierCodeAndCustomerCode(@Param("supplierCode") String supplierCode, @Param("customerCode") String customerCode);

    /**
     * 查询物料分类与订单类型
     */
    List<DeliveryPlan> listAllGroupByMaterialTypeAndOrderType();

    String selectDeliveryPlanLatestVersion();

    /**
     * 查询上一版项次
     * @param mrpComputerNo
     * @return
     */
    List<PreVersionDeliveryPlanDetailItemVo> selectPreVersionItem(@Param("mrpComputerNo") String mrpComputerNo);
}
