package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.QuotationAttRequireAttachment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bncloud.quotation.vo.QuotationAttRequireAttachmentVo;
import net.bncloud.quotation.param.QuotationAttRequireAttachmentParam;
import net.bncloud.common.base.domain.QueryParam;
import java.util.List;
/**
 * <p>
 * 附件需求上传文件表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-04
 */
public interface QuotationAttRequireAttachmentMapper extends BaseMapper<QuotationAttRequireAttachment> {
		/**
		 * <p>
		 * 自定义分页
		 * </p>
		 *
		 * @author Auto-generator
		 * @since 2022-03-04
		 */
		List<QuotationAttRequireAttachment> selectListPage(IPage page,QueryParam<QuotationAttRequireAttachmentParam> pageParam);
}
