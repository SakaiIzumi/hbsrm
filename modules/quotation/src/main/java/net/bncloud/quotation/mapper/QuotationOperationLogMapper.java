package net.bncloud.quotation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationOperationLog;
import net.bncloud.quotation.param.QuotationOperationLogParam;
import net.bncloud.quotation.vo.QuotationOperationLogVo;

import java.util.List;
/**
 * <p>
 * 询价单操作记录日志表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-02
 */
public interface QuotationOperationLogMapper extends BaseMapper<QuotationOperationLog> {
		/**
		 * <p>
		 * 自定义分页
		 * </p>
		 *
		 * @author Auto-generator
		 * @since 2022-03-02
		 */
		List<QuotationOperationLogVo> selectListPage(IPage page, QueryParam<QuotationOperationLogParam> pageParam);
}
