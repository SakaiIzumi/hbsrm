package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.bncloud.common.api.R;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.quotation.entity.QuotationAttRequireAttachment;
import net.bncloud.quotation.entity.QuotationBase;
import net.bncloud.quotation.mapper.QuotationAttRequireAttachmentMapper;
import net.bncloud.quotation.mapper.QuotationBaseMapper;
import net.bncloud.quotation.service.IQuotationAttRequireAttachmentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.quotation.service.QuotationBaseService;
import net.bncloud.quotation.vo.QuotationAttRequireAttachmentVo;
import net.bncloud.quotation.param.QuotationAttRequireAttachmentParam;
import net.bncloud.service.api.file.dto.FileInfoDto;
import net.bncloud.service.api.file.feign.FileCenterFeignClient;
import net.bncloud.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static net.bncloud.quotation.enums.QuotationResultCode.SOURCE_NOT_FOUND;
import static net.bncloud.quotation.enums.QuotationResultCode.SUPPLIER_NOT_FOUND;

/**
 * 附件需求上传文件表 服务实现类
 *
 * @author Auto-generator
 * @since 2022-03-04
 */
@Service
public class QuotationAttRequireAttachmentServiceImpl extends BaseServiceImpl<QuotationAttRequireAttachmentMapper, QuotationAttRequireAttachment> implements IQuotationAttRequireAttachmentService {


	@Autowired
	QuotationBaseMapper quotationBaseMapper;

		@Override
		public IPage<QuotationAttRequireAttachment> selectPage(IPage<QuotationAttRequireAttachment> page, QueryParam<QuotationAttRequireAttachmentParam> pageParam) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		//notice.setTenantId(SecureUtil.getTenantId());
		return page.setRecords(baseMapper.selectListPage(page, pageParam));
		}

	@Override
	public List<QuotationAttRequireAttachment>  saveFile(Long quotationId, Long quotationAttRequireId, List<FileInfoDto> fileInfoDtos) {
		QuotationBase quotationBase = quotationBaseMapper.selectById(quotationId);
		if (quotationBase == null) {
			throw new BizException(SOURCE_NOT_FOUND);
		}
		Long supplierId = AuthUtil.getUser().getCurrentSupplier().getSupplierId();
		if (supplierId == null) {
			throw new BizException(SUPPLIER_NOT_FOUND);
		}
		List<QuotationAttRequireAttachment> quotationAttachmentList = new ArrayList<>();
		for (FileInfoDto fileInfoDto : fileInfoDtos) {
			QuotationAttRequireAttachment quotationAttRequireAttachment = new QuotationAttRequireAttachment();
			quotationAttRequireAttachment.setFileId(fileInfoDto.getId());
			quotationAttRequireAttachment.setFileUrl(fileInfoDto.getUrl());
			quotationAttRequireAttachment.setFileName(fileInfoDto.getOriginalFilename());
			//文件类型不取ContentType
//			quotationAttRequireAttachment.setFileType(fileInfoDto.getExtension());
			quotationAttRequireAttachment.setQuotationBaseId(quotationId);
			quotationAttRequireAttachment.setQuotationAttRequireId(quotationAttRequireId);
			quotationAttRequireAttachment.setSupplierId(supplierId);
			quotationAttRequireAttachment.setRoundNumber(quotationBase.getCurrentRoundNumber());
			quotationAttachmentList.add(quotationAttRequireAttachment);
		}
		saveBatch(quotationAttachmentList);

		return quotationAttachmentList;
	}

	@Override
	public List<QuotationAttRequireAttachmentVo> getFilesByQuotationIdAndAttRequireId(Long quotationId, Long quotationAttRequireId) {
		QuotationBase quotationBase = quotationBaseMapper.selectById(quotationId);
		if(quotationBase == null){
			throw new BizException(SOURCE_NOT_FOUND);
		}
		Long supplierId = AuthUtil.getUser().getCurrentSupplier().getSupplierId();
		if(supplierId == null){
			throw new BizException(SUPPLIER_NOT_FOUND);
		}
		LambdaQueryWrapper<QuotationAttRequireAttachment> quotationAttRequireAttachmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
		quotationAttRequireAttachmentLambdaQueryWrapper.eq(QuotationAttRequireAttachment::getQuotationBaseId,quotationId)
				.eq(QuotationAttRequireAttachment::getQuotationAttRequireId,quotationAttRequireId)
				.eq(QuotationAttRequireAttachment::getRoundNumber,quotationBase.getCurrentRoundNumber())
				.eq(QuotationAttRequireAttachment::getSupplierId,supplierId);
		List<QuotationAttRequireAttachment> quotationAttRequireAttachments = baseMapper.selectList(quotationAttRequireAttachmentLambdaQueryWrapper);
		List<QuotationAttRequireAttachmentVo> quotationAttRequireAttachmentVoList = new ArrayList<>();
		for (int i = 0; i < quotationAttRequireAttachments.size(); i++) {
			QuotationAttRequireAttachmentVo quotationAttRequireAttachmentVo = new QuotationAttRequireAttachmentVo();
			BeanUtil.copy(quotationAttRequireAttachments.get(i),quotationAttRequireAttachmentVo);
			quotationAttRequireAttachmentVoList.add(quotationAttRequireAttachmentVo);
		}
		return quotationAttRequireAttachmentVoList;
	}

	@Override
	public void deleteByAttRequireId(Long quotationAttRequireId, Long quotationId) {
		QuotationBase quotationBase = quotationBaseMapper.selectById(quotationId);
		if(quotationBase == null){
			throw new BizException(SOURCE_NOT_FOUND);
		}
		Long supplierId = AuthUtil.getUser().getCurrentSupplier().getSupplierId();
		if(supplierId == null){
			throw new BizException(SUPPLIER_NOT_FOUND);
		}
		LambdaQueryWrapper<QuotationAttRequireAttachment> quotationAttRequireAttachmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
		quotationAttRequireAttachmentLambdaQueryWrapper.eq(QuotationAttRequireAttachment::getQuotationBaseId,quotationId)
				.eq(QuotationAttRequireAttachment::getQuotationAttRequireId,quotationAttRequireId)
				.eq(QuotationAttRequireAttachment::getRoundNumber,quotationBase.getCurrentRoundNumber())
				.eq(QuotationAttRequireAttachment::getSupplierId,supplierId);
		baseMapper.delete(quotationAttRequireAttachmentLambdaQueryWrapper);
	}

}
