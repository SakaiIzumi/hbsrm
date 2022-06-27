package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.MaterialTemplateExt;
import net.bncloud.quotation.mapper.MaterialTemplateExtMapper;
import net.bncloud.quotation.param.MaterialTemplateExtParam;
import net.bncloud.quotation.service.MaterialTemplateExtService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 物料报价模板扩展信息（物料、公式信息） 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
public class MaterialTemplateExtServiceImpl extends BaseServiceImpl<MaterialTemplateExtMapper, MaterialTemplateExt> implements MaterialTemplateExtService {

    @Override
    public IPage<MaterialTemplateExt> selectPage(IPage<MaterialTemplateExt> page, QueryParam<MaterialTemplateExtParam> pageParam) {
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }

    @Override
    public void clearTemplateExt(Long templateId) {
    	baseMapper.deleteByTemplateId(templateId);
    }
}
