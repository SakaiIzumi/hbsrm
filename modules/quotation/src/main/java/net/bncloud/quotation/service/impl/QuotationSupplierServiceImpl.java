package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.quotation.entity.QuotationSupplier;
import net.bncloud.quotation.enums.QuotationSupplierResponseStatus;
import net.bncloud.quotation.mapper.QuotationSupplierMapper;
import net.bncloud.quotation.param.QuotationSupplierParam;
import net.bncloud.quotation.service.QuotationSupplierService;
import net.bncloud.quotation.vo.QuotationSupplierVo;
import net.bncloud.support.Condition;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 询价供应商信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-18
 */
@Service
public class QuotationSupplierServiceImpl extends BaseServiceImpl<QuotationSupplierMapper, QuotationSupplier> implements QuotationSupplierService {


	@Override
    public IPage<QuotationSupplierVo> selectPage(IPage<QuotationSupplierVo> page, QueryParam<QuotationSupplierParam> pageParam) {
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }



	@Override
	public List<QuotationSupplier> queryList(QuotationSupplier quotationSupplier,Long supplierId) {
		LambdaQueryWrapper<QuotationSupplier> queryWrapper = Condition.getQueryWrapper(new QuotationSupplier())
				.lambda().eq(QuotationSupplier::getQuotationBaseId, quotationSupplier.getQuotationBaseId())
				.eq(supplierId!=null,QuotationSupplier::getSupplierId,supplierId)
				.orderByDesc(QuotationSupplier::getCreatedDate);
		return super.list(queryWrapper);
	}

	@Override
	public void deleteByQuotationBaseId(Long quotationBaseId) {
		baseMapper.deleteByQuotationBaseId(quotationBaseId);
	}

	/**
	 * 修改供应商响应状态
	 * @param quotationBaseId 询价单基础信息ID
	 * @param supplierId 供应商ID
	 */
    @Override
    public void updateResponseStatus(Long quotationBaseId, Long supplierId, String responseStatus) {
    	update(Wrappers.<QuotationSupplier>update().lambda()
				.set(QuotationSupplier ::getResponseStatus,responseStatus)
				.set(QuotationSupplier::getResponseTime,new Date())
				.eq(QuotationSupplier::getQuotationBaseId,quotationBaseId)
				.eq(QuotationSupplier::getSupplierId,supplierId)
		);

    }

	/**
	 * 批量修改供应商响应状态
	 * @param quotationBaseId 	询价单基础信息ID
	 * @param supplierIds 		供应商ID 集合
	 * @param responseStatus	响应状态
	 */
	@Override
	public void updateResponseStatusBatch(Long quotationBaseId, Long[] supplierIds, String responseStatus) {
		if(supplierIds !=null && supplierIds.length > 0){
			for (Long supplierId : supplierIds) {
			    updateResponseStatus(quotationBaseId,supplierId,responseStatus);
			}
		}
	}

	/**
	 * 统计已报价供应商数量
	 * @param quotationBaseId 询价单基础信息ID
	 * @return 已报价供应商数量
	 */
    @Override
    public Integer countBiddingNum(Long quotationBaseId) {
		QuotationSupplier quotationSupplier = new QuotationSupplier();
		quotationSupplier.setQuotationBaseId(quotationBaseId);
		quotationSupplier.setResponseStatus(QuotationSupplierResponseStatus.bid.name());
		LambdaQueryWrapper<QuotationSupplier> queryWrapper = Condition.getQueryWrapper(quotationSupplier).lambda();
		return this.count(queryWrapper);
    }
}
