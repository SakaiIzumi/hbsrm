package net.bncloud.quotation.vo;


import net.bncloud.quotation.entity.MaterialQuotationTemplate;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.quotation.entity.MaterialTemplateExt;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 物料报价模板
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
public class MaterialQuotationTemplateVo extends MaterialQuotationTemplate implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 物料报价模板扩展信息（物料、公式信息）
     */
    private List<MaterialTemplateExt> materialTemplateExtList;

    /**
     * 是否调用公式结果计算测试按钮并且返回状态为true
     */
    private Boolean calculateSuccessType;

}
