package net.bncloud.quotation.service;

import net.bncloud.quotation.entity.QuotationLineBase;
import net.bncloud.quotation.vo.QuotationBaseVo;
import net.bncloud.quotation.vo.QuotationLineBaseVo;
import net.bncloud.quotation.param.QuotationLineBaseParam;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;

/**
 * <p>
 * 询价行基础信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface QuotationLineBaseService extends BaseService<QuotationLineBase> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<QuotationLineBase> selectPage(IPage<QuotationLineBase> page, QueryParam<QuotationLineBaseParam> pageParam);


	/**
	 * 保存询价行信息
	 * @param quotationLineBase 询价行信息
	 */
	void saveInfo(QuotationLineBaseVo quotationLineBase);

	/**
	 * 修改询价行信息
	 * @param quotationLineBase 询价行信息
	 */
	void updateInfo(QuotationLineBaseVo quotationLineBase);

	/**
	 * 根据询价单基础信息ID查询询价行信息
	 * @param quotationBaseId 询价单基础信息ID
	 * @return
	 */
    QuotationLineBaseVo getByQuotationBaseId(Long quotationBaseId);
}
