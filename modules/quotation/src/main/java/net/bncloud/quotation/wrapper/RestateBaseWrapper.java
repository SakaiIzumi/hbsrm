package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.RestateBase;
import net.bncloud.quotation.vo.RestateBaseVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 询价重报基础信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-03-08
 */
public class RestateBaseWrapper extends BaseEntityWrapper<RestateBase,RestateBaseVo>  {

	public static RestateBaseWrapper build() {
		return new RestateBaseWrapper();
	}

	@Override
	public RestateBaseVo entityVO(RestateBase entity) {
        RestateBaseVo entityVo = BeanUtil.copy(entity, RestateBaseVo.class);
		return entityVo;
	}



}
