package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.RestateSupplier;
import net.bncloud.quotation.vo.RestateSupplierVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 询价重报供应商邀请信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-03-08
 */
public class RestateSupplierWrapper extends BaseEntityWrapper<RestateSupplier,RestateSupplierVo>  {

	public static RestateSupplierWrapper build() {
		return new RestateSupplierWrapper();
	}

	@Override
	public RestateSupplierVo entityVO(RestateSupplier entity) {
        RestateSupplierVo entityVo = BeanUtil.copy(entity, RestateSupplierVo.class);
		return entityVo;
	}



}
