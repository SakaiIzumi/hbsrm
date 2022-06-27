package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialAttachment;
import net.bncloud.financial.vo.FinancialAttachmentVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 对账附件信息表
 * </p>
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountAttachmentWrapper extends BaseEntityWrapper<FinancialAttachment, FinancialAttachmentVo> {

    public static AccountAttachmentWrapper build() {
        return new AccountAttachmentWrapper();
    }

    @Override
    public FinancialAttachmentVo entityVO(FinancialAttachment entity) {
        FinancialAttachmentVo entityVo = BeanUtil.copy(entity, FinancialAttachmentVo.class);
        return entityVo;
    }


}
