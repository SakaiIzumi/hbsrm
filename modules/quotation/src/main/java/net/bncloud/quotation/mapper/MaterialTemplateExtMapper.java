package net.bncloud.quotation.mapper;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.MaterialTemplateExt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.quotation.param.MaterialTemplateExtParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 物料报价模板扩展信息（物料、公式信息） Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface MaterialTemplateExtMapper extends BaseMapper<MaterialTemplateExt> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-02-14
     */
    List<MaterialTemplateExt> selectListPage(IPage page, QueryParam<MaterialTemplateExtParam> pageParam);

	/**
	 * 删除扩展信息
	 * @param templateId 模板ID
	 */
	void deleteByTemplateId(@Param("templateId") Long templateId);
}
