package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationAttachment;
import net.bncloud.quotation.param.QuotationAttachmentParam;
import net.bncloud.quotation.vo.PricingInfoForRemarkAndFileVo;
import net.bncloud.quotation.vo.QuotationAttachmentVo;
import net.bncloud.service.api.file.dto.FileInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 附件信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface QuotationAttachmentService extends BaseService<QuotationAttachment> {

		/**
         * 自定义分页
         * @param page
         * @param pageParam
         * @return
         */
		IPage<QuotationAttachment> selectPage(IPage<QuotationAttachment> page, QueryParam<QuotationAttachmentParam> pageParam);

	void saveFile(Long businessFormId, String businessType, List<FileInfoDto> fileInfoDtos) throws Exception;

	List<QuotationAttachmentVo> listFiles(String businessFormId, String businessType);

	/**
	 * 保存文件信息
	 * @param quotationAttachment
	 */
	void saveInfo(QuotationAttachment quotationAttachment);

	/**
	 * 设置附件
	 * @param pricingInfoForRemarkAndFileVo
	 */
	void buildAttachment(PricingInfoForRemarkAndFileVo pricingInfoForRemarkAndFileVo);


}
