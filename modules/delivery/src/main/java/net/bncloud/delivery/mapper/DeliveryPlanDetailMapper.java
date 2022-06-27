package net.bncloud.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryPlanDetail;
import net.bncloud.delivery.param.DeliveryPlanDetailParam;
import net.bncloud.delivery.param.PlanSchedulingBoardParam;
import net.bncloud.delivery.vo.PlanSchedulingBoardVo;
import net.bncloud.delivery.vo.PlanSchedulingDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
public interface DeliveryPlanDetailMapper extends BaseMapper<DeliveryPlanDetail> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-01-17
     */
    List<PlanSchedulingDetailVo> selectListPage(IPage<PlanSchedulingDetailVo> page, @Param("pageParam") QueryParam<DeliveryPlanDetailParam> pageParam);

	/**
	 * 查询发货计划详情
	 * @param sourceId 来源系统ID
	 * @return
	 */
	DeliveryPlanDetail queryOneBySourceId(@Param("sourceId") String sourceId);

	/**
	 * 需要留意的是此方法返回了空的 条码数据
	 */
    List<DeliveryPlanDetail> listAllGroupByProductCodeAndMerchantCode();

	List<PlanSchedulingBoardVo> selectZcPlanSchedulingBoardPage(IPage<PlanSchedulingBoardVo> page, @Param("queryParam") QueryParam<PlanSchedulingBoardParam> queryParam);
	List<PlanSchedulingBoardVo> selectZyPlanSchedulingBoardPage(IPage<PlanSchedulingBoardVo> page, @Param("queryParam") QueryParam<PlanSchedulingBoardParam> queryParam);
}
