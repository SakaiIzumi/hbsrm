package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.api.feign.file.FileInfo;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.quotation.entity.QuotationAttachment;
import net.bncloud.quotation.mapper.QuotationAttachmentMapper;
import net.bncloud.quotation.param.QuotationAttachmentParam;
import net.bncloud.quotation.service.QuotationAttachmentService;
import net.bncloud.quotation.vo.PricingInfoForRemarkAndFileVo;
import net.bncloud.quotation.vo.QuotationAttachmentVo;
import net.bncloud.service.api.file.dto.FileInfoDto;
import net.bncloud.service.api.file.feign.FileCenterFeignClient;
import net.bncloud.utils.AuthUtil;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 附件信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
public class QuotationAttachmentServiceImpl extends BaseServiceImpl<QuotationAttachmentMapper, QuotationAttachment> implements QuotationAttachmentService {

		@Override
		public IPage<QuotationAttachment> selectPage(IPage<QuotationAttachment> page, QueryParam<QuotationAttachmentParam> pageParam) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		//notice.setTenantId(SecureUtil.getTenantId());
		return page.setRecords(baseMapper.selectListPage(page, pageParam));
		}

	@Override
	public void saveFile(Long businessFormId, String businessType, List<FileInfoDto> fileInfoDtos) throws Exception {
		BaseUserEntity user = AuthUtil.getUser();
		String userName = user.getUserName();
		List<QuotationAttachment> quotationAttachmentList = new ArrayList<>();
		for (FileInfoDto fileInfoDto : fileInfoDtos) {
			QuotationAttachment quotationAttachment = new QuotationAttachment();
			quotationAttachment.setFileId(fileInfoDto.getId());
			quotationAttachment.setFileUrl(fileInfoDto.getUrl());
			quotationAttachment.setFileName(fileInfoDto.getOriginalFilename());
			//文件类型不取ContentType
//			quotationAttachment.setFileType(fileInfoDto.getContentType());
			quotationAttachment.setBusinessType(businessType);
			quotationAttachment.setBusinessFormId(businessFormId);
			quotationAttachment.setCreatedByName(userName);
			quotationAttachmentList.add(quotationAttachment);
		}
		saveBatch(quotationAttachmentList);
	}

	@Override
	public List<QuotationAttachmentVo> listFiles(String businessFormId, String businessType) {
		LambdaQueryWrapper<QuotationAttachment> quotationAttachmentQueryWrapper = new LambdaQueryWrapper<>();
		quotationAttachmentQueryWrapper.eq(QuotationAttachment::getBusinessFormId,businessFormId).eq(QuotationAttachment::getBusinessType,businessType).orderByAsc(QuotationAttachment::getCreatedDate);
		List<QuotationAttachment> quotationAttachments = baseMapper.selectList(quotationAttachmentQueryWrapper);
		List<QuotationAttachmentVo> quotationAttachmentVos = new ArrayList<>();
		//加个标识
		for (int i = 0; i < quotationAttachments.size(); i++) {
			QuotationAttachmentVo quotationAttachmentVo = new QuotationAttachmentVo();
			BeanUtil.copy(quotationAttachments.get(i), quotationAttachmentVo);
			quotationAttachmentVo.setItemNo(i + 1);
		}
		return quotationAttachmentVos;
	}

	/**
	 * 保存文件信息
	 * @param quotationAttachment 文件信息
	 */
	@Override
	public void saveInfo(QuotationAttachment quotationAttachment) {
		BaseUserEntity user = AuthUtil.getUser();
		quotationAttachment.setId(null);
		quotationAttachment.setCreatedByName(user.getUserName());
		save(quotationAttachment);
	}

	@Override
	public void buildAttachment(PricingInfoForRemarkAndFileVo pricingInfoForRemarkAndFileVo) {
		List<QuotationAttachment> quotationAttachments = pricingInfoForRemarkAndFileVo.getQuotationAttachments();
		if(CollectionUtil.isNotEmpty(quotationAttachments)){
			List<FileInfo> attachmentList = new ArrayList<>();
			for (QuotationAttachment quotationAttachment : quotationAttachments) {
				FileInfo attachment = FileInfo.builder()
						.id(Long.valueOf(quotationAttachment.getFileId()))
						.originalFilename(quotationAttachment.getFileName())
						.url(quotationAttachment.getFileUrl())
						.build();
				attachmentList.add(attachment);
			}
			pricingInfoForRemarkAndFileVo.setAttachmentList(attachmentList);
		}
	}

}
