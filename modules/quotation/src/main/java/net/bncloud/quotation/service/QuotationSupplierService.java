package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationSupplier;
import net.bncloud.quotation.param.QuotationSupplierParam;
import net.bncloud.quotation.vo.QuotationSupplierVo;

import java.util.List;

/**
 * <p>
 * 询价供应商信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-18
 */
public interface QuotationSupplierService extends BaseService<QuotationSupplier> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<QuotationSupplierVo> selectPage(IPage<QuotationSupplierVo> page, QueryParam<QuotationSupplierParam> pageParam);



    List<QuotationSupplier> queryList(QuotationSupplier quotationSupplier,Long supplierId);


	/**
	 * 删除供应商信息
	 * @param quotationBaseId 询价单ID
	 */
	void deleteByQuotationBaseId(Long quotationBaseId);

	/**
	 * 修改供应商响应状态
	 * @param quotationBaseId 询价单基础信息ID
	 * @param supplierId 供应商ID
	 * @param responseStatus 响应状态
	 */
	void updateResponseStatus(Long quotationBaseId, Long supplierId, String responseStatus);

	/**
	 * 批量修改供应商响应状态
	 * @param quotationBaseId 	询价单基础信息ID
	 * @param supplierIds 		供应商ID 集合
	 * @param responseStatus	响应状态
	 */
	void updateResponseStatusBatch(Long quotationBaseId, Long[] supplierIds, String responseStatus);

	/**
	 * 统计已报价供应商数量
	 * @param quotationBaseId 询价单基础信息ID
	 * @return 已报价供应商数量
	 */
	Integer countBiddingNum(Long quotationBaseId);
}
