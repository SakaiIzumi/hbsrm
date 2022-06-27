package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationAttRequire;
import net.bncloud.quotation.mapper.QuotationAttRequireMapper;
import net.bncloud.quotation.param.QuotationAttRequireParam;
import net.bncloud.quotation.param.SupplierAttRequireParam;
import net.bncloud.quotation.service.QuotationAttRequireService;
import net.bncloud.quotation.vo.SupplierAttachmentRequireVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 附件需求清单 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
public class QuotationAttRequireServiceImpl extends BaseServiceImpl<QuotationAttRequireMapper, QuotationAttRequire> implements QuotationAttRequireService {

	private final QuotationAttRequireMapper quotationAttRequireMapper;
	public QuotationAttRequireServiceImpl(QuotationAttRequireMapper quotationAttRequireMapper) {
		this.quotationAttRequireMapper = quotationAttRequireMapper;
	}

	@Override
		public IPage<QuotationAttRequire> selectPage(IPage<QuotationAttRequire> page, QueryParam<QuotationAttRequireParam> pageParam) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		//notice.setTenantId(SecureUtil.getTenantId());
		return page.setRecords(baseMapper.selectListPage(page, pageParam));
		}

	@Override
	public IPage<SupplierAttachmentRequireVo> selectSupplierAttRequirePage(IPage<SupplierAttachmentRequireVo> page, QueryParam<SupplierAttRequireParam> queryParam) {
		return page.setRecords(quotationAttRequireMapper.selectSupplierAttRequire(page, queryParam));
	}
}
