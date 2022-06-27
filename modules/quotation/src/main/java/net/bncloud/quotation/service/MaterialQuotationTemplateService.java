package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.MaterialQuotationTemplate;
import net.bncloud.quotation.param.MaterialQuotationTemplateParam;
import net.bncloud.quotation.vo.MaterialQuotationTemplateVo;

/**
 * <p>
 * 物料报价模板 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface MaterialQuotationTemplateService extends BaseService<MaterialQuotationTemplate> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<MaterialQuotationTemplate> selectPage(IPage<MaterialQuotationTemplate> page, QueryParam<MaterialQuotationTemplateParam> pageParam);


	/**
	 * 新增物料模板信息
	 * @param materialQuotationTemplate 物料询价模板信息
	 */
	void saveInfo(MaterialQuotationTemplateVo materialQuotationTemplate);

	/**
	 * 校验计算表达式
	 * @param materialQuotationTemplate 物料询价模板
	 */
	void validateExpression(MaterialQuotationTemplateVo materialQuotationTemplate);

	/**
	 * 更新物料模板信息
	 * @param materialQuotationTemplate 物料询价模板
	 */
	void updateInfo(MaterialQuotationTemplateVo materialQuotationTemplate);


	/**
	 * 查询询价模板详情
	 * @param id 主键ID
	 * @return 详情信息
	 */
	MaterialQuotationTemplateVo getInfoById(Long id);

	/**
	 * 表达式结果计算
	 * @param materialQuotationTemplate 询价模板
	 * @return 模板信息
	 */
	MaterialQuotationTemplateVo calculate(MaterialQuotationTemplateVo materialQuotationTemplate);

	/**
	 * 作废
	 * @param id 主键ID
	 */
	void disable(long id);
}
