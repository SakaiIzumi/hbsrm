package net.bncloud.delivery.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.delivery.entity.DeliveryPlan;
import net.bncloud.delivery.entity.DeliveryPlanDetail;
import net.bncloud.delivery.mapper.DeliveryPlanDetailMapper;
import net.bncloud.delivery.param.DeliveryPlanDetailParam;
import net.bncloud.delivery.service.DeliveryPlanDetailService;
import net.bncloud.delivery.service.DeliveryPlanService;
import net.bncloud.delivery.vo.PlanSchedulingDetailVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
@Service
@Slf4j
public class DeliveryPlanDetailServiceImpl extends BaseServiceImpl<DeliveryPlanDetailMapper, DeliveryPlanDetail> implements DeliveryPlanDetailService {

    @Resource
    @Lazy
    private DeliveryPlanService deliveryPlanService;

    @Override
    public IPage<PlanSchedulingDetailVo> selectPage(IPage<PlanSchedulingDetailVo> page, QueryParam<DeliveryPlanDetailParam> queryParam) {
        List<PlanSchedulingDetailVo> planSchedulingDetailVos = baseMapper.selectListPage(page, queryParam);
        //设置采购方名称和编码
        Optional.ofNullable(planSchedulingDetailVos).ifPresent(detailList->{
            detailList.forEach(detail->{
                DeliveryPlan deliveryPlan = deliveryPlanService.getById(detail.getDeliveryPlanId());
                if (ObjectUtil.isNotEmpty(deliveryPlan)) {
                    detail.setPurchaseCode(deliveryPlan.getPurchaseCode());
                    detail.setPurchaseName(deliveryPlan.getPurchaseName());
                }
            });
        });
        return page.setRecords(planSchedulingDetailVos);
    }

    @Override
    public DeliveryPlanDetail queryOneBySourceId(String sourceId) {
        DeliveryPlanDetail deliveryPlanDetail = null;
        if (StringUtil.isNotBlank(sourceId)){
            deliveryPlanDetail = baseMapper.queryOneBySourceId(sourceId);
        }
        return deliveryPlanDetail;
    }

    @Override
    public List<DeliveryPlanDetail> listAllGroupByProductCodeAndMerchantCode() {
        return this.baseMapper.listAllGroupByProductCodeAndMerchantCode();
    }
}
