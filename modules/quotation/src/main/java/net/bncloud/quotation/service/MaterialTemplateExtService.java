package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.MaterialTemplateExt;
import net.bncloud.quotation.param.MaterialTemplateExtParam;

/**
 * <p>
 * 物料报价模板扩展信息（物料、公式信息） 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface MaterialTemplateExtService extends BaseService<MaterialTemplateExt> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<MaterialTemplateExt> selectPage(IPage<MaterialTemplateExt> page, QueryParam<MaterialTemplateExtParam> pageParam);


	/**
	 * 清除扩展信息
	 * @param templateId 模板ID
	 */
	void clearTemplateExt(Long templateId);
}
