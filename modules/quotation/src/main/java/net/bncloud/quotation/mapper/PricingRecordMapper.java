package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.PricingRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.quotation.vo.PricingRecordVo;
import net.bncloud.quotation.param.PricingRecordParam;
import net.bncloud.common.base.domain.QueryParam;
import java.util.List;
/**
 * <p>
 * 定价请示记录信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface PricingRecordMapper extends BaseMapper<PricingRecord> {
		/**
		 * <p>
		 * 自定义分页
		 * </p>
		 *
		 * @author Auto-generator
		 * @since 2022-02-14
		 */
		List<PricingRecord> selectListPage(IPage page, QueryParam<PricingRecordParam> pageParam);
}
