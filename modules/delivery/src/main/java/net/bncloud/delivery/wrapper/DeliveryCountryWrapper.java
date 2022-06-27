package net.bncloud.delivery.wrapper;


import net.bncloud.delivery.entity.DeliveryCountry;
import net.bncloud.delivery.vo.DeliveryCountryVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 国家信息表
 * </p>
 *包装类,返回视图层所需的字段
 * @author huangtao
 * @since 2021-03-17
 */
public class DeliveryCountryWrapper extends BaseEntityWrapper<DeliveryCountry,DeliveryCountryVo>  {

	public static DeliveryCountryWrapper build() {
		return new DeliveryCountryWrapper();
	}

	@Override
	public DeliveryCountryVo entityVO(DeliveryCountry entity) {
        DeliveryCountryVo entityVo = BeanUtil.copy(entity, DeliveryCountryVo.class);
		return entityVo;
	}



}
