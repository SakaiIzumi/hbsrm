package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationBase;
import net.bncloud.quotation.entity.QuotationLineExt;
import net.bncloud.quotation.param.QuotationLineExtParam;
import net.bncloud.quotation.vo.QuotationLineExtVo;

import java.util.List;

/**
 * <p>
 * 询价行动态行扩展信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface QuotationLineExtService extends BaseService<QuotationLineExt> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<QuotationLineExt> selectPage(IPage<QuotationLineExt> page, QueryParam<QuotationLineExtParam> pageParam);


	/**
	 * 删除询价行扩展信息
	 * @param quotationBaseId 询价基础信息ID
	 */
	void deleteByQuotationBaseId(Long quotationBaseId);

	List<QuotationLineExt> queryList(QuotationLineExt quotationLineExt);

    List<QuotationLineExtVo> selectQuotationLineExtlist(QuotationLineExt quotationLineExt);
}
