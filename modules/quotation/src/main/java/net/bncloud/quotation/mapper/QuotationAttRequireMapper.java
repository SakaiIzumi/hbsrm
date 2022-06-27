package net.bncloud.quotation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationAttRequire;
import net.bncloud.quotation.param.QuotationAttRequireParam;
import net.bncloud.quotation.param.SupplierAttRequireParam;
import net.bncloud.quotation.vo.SupplierAttachmentRequireVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * <p>
 * 附件需求清单 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Mapper
public interface QuotationAttRequireMapper extends BaseMapper<QuotationAttRequire> {
		/**
		 * <p>
		 * 自定义分页
		 * </p>
		 *
		 * @author Auto-generator
		 * @since 2022-02-14
		 */
		List<QuotationAttRequire> selectListPage(IPage page, QueryParam<QuotationAttRequireParam> pageParam);

		List<SupplierAttachmentRequireVo> selectSupplierAttRequire(IPage page,@Param("queryParam") QueryParam<SupplierAttRequireParam> queryParam);
}
