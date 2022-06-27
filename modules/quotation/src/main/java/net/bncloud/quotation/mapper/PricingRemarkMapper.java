package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.PricingRemark;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.quotation.param.PricingRemarkParam;
import net.bncloud.common.base.domain.QueryParam;
import java.util.List;
/**
 * <p>
 * 定价说明信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface PricingRemarkMapper extends BaseMapper<PricingRemark> {
		/**
		 * <p>
		 * 自定义分页
		 * </p>
		 *
		 * @author Auto-generator
		 * @since 2022-02-14
		 */
		List<PricingRemark> selectListPage(IPage page, QueryParam<PricingRemarkParam> pageParam);
}
