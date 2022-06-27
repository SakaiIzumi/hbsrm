package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.QuotationEquipment;
import net.bncloud.quotation.vo.QuotationEquipmentVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 设备能力要求信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class QuotationEquipmentWrapper extends BaseEntityWrapper<QuotationEquipment,QuotationEquipmentVo>  {

	public static QuotationEquipmentWrapper build() {
		return new QuotationEquipmentWrapper();
	}

	@Override
	public QuotationEquipmentVo entityVO(QuotationEquipment entity) {
        QuotationEquipmentVo entityVo = BeanUtil.copy(entity, QuotationEquipmentVo.class);
		return entityVo;
	}



}
