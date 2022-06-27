package net.bncloud.quotation.service;

import net.bncloud.quotation.entity.QuotationAttRequireAttachment;
import net.bncloud.quotation.vo.QuotationAttRequireAttachmentVo;
import net.bncloud.quotation.param.QuotationAttRequireAttachmentParam;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.service.api.file.dto.FileInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 附件需求上传文件表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-04
 */
public interface IQuotationAttRequireAttachmentService extends BaseService<QuotationAttRequireAttachment> {

		/**
         * 自定义分页
         * @param page
         * @param pageParam
         * @return
         */
		IPage<QuotationAttRequireAttachment> selectPage(IPage<QuotationAttRequireAttachment> page, QueryParam<QuotationAttRequireAttachmentParam> pageParam);


	List<QuotationAttRequireAttachment>  saveFile(Long quotationId, Long quotationAttRequireId, List<FileInfoDto> fileInfoDtos);

	List<QuotationAttRequireAttachmentVo> getFilesByQuotationIdAndAttRequireId(Long quotationId, Long quotationAttRequireId);

    void deleteByAttRequireId(Long quotationAttRequireId, Long quotationId);
}
