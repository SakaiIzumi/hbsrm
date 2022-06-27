package net.bncloud.quotation.vo;


import net.bncloud.quotation.entity.QuotationLineBase;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.quotation.entity.QuotationLineExt;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 询价行基础信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
public class QuotationLineBaseVo extends QuotationLineBase implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 询价行动态行扩展信息
     */
    private List<QuotationLineExt> quotationLineExtList;

}
