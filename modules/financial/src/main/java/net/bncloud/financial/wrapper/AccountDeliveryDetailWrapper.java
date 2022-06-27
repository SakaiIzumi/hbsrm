package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialDeliveryDetail;
import net.bncloud.financial.vo.FinancialDeliveryDetailVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 出货明细表信息表
 * </p>
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountDeliveryDetailWrapper extends BaseEntityWrapper<FinancialDeliveryDetail, FinancialDeliveryDetailVo> {

    public static AccountDeliveryDetailWrapper build() {
        return new AccountDeliveryDetailWrapper();
    }

    @Override
    public FinancialDeliveryDetailVo entityVO(FinancialDeliveryDetail entity) {
        FinancialDeliveryDetailVo entityVo = BeanUtil.copy(entity, FinancialDeliveryDetailVo.class);
        return entityVo;
    }


}
