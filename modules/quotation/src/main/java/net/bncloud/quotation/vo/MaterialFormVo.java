package net.bncloud.quotation.vo;


import net.bncloud.quotation.entity.MaterialForm;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.quotation.entity.MaterialFormExt;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 物料表单信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
public class MaterialFormVo extends MaterialForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物料表单扩展信息
     */
    private List<MaterialFormExt> materialFormExtList;


}
