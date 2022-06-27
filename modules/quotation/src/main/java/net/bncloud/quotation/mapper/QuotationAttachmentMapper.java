package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.QuotationAttachment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.quotation.vo.QuotationAttachmentVo;
import net.bncloud.quotation.param.QuotationAttachmentParam;
import net.bncloud.common.base.domain.QueryParam;
import java.util.List;
/**
 * <p>
 * 附件信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface QuotationAttachmentMapper extends BaseMapper<QuotationAttachment> {
		/**
		 * <p>
		 * 自定义分页
		 * </p>
		 *
		 * @author Auto-generator
		 * @since 2022-02-14
		 */
		List<QuotationAttachment> selectListPage(IPage page, QueryParam<QuotationAttachmentParam> pageParam);
}
