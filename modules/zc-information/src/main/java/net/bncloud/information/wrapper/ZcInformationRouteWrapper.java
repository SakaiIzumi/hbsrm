package net.bncloud.information.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.information.entity.ZcInformationRoute;
import net.bncloud.information.vo.ZcInformationRouteVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 消息路由表
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2021-04-21
 */
public class ZcInformationRouteWrapper extends BaseEntityWrapper<ZcInformationRoute, ZcInformationRouteVo>  {

	public static ZcInformationRouteWrapper build() {
		return new ZcInformationRouteWrapper();
	}

	@Override
	public ZcInformationRouteVo entityVO(ZcInformationRoute entity) {
        ZcInformationRouteVo entityVo = BeanUtil.copy(entity, ZcInformationRouteVo.class);
		return entityVo;
	}



}
