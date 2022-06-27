package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryPlanDetail;
import net.bncloud.delivery.param.DeliveryPlanDetailParam;
import net.bncloud.delivery.vo.PlanSchedulingDetailVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
public interface DeliveryPlanDetailService extends BaseService<DeliveryPlanDetail> {

    /**
     * 自定义分页
     *
     * @param page
     * @param queryParam
     * @return
     */
    IPage<PlanSchedulingDetailVo> selectPage(IPage<PlanSchedulingDetailVo> page, QueryParam<DeliveryPlanDetailParam> queryParam);


    DeliveryPlanDetail queryOneBySourceId(String sourceId);

    /**
     * 查询全部 条码和物料编码的转换
     * @return
     */
    List<DeliveryPlanDetail> listAllGroupByProductCodeAndMerchantCode();

}
