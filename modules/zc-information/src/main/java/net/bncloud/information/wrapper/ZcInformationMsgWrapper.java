package net.bncloud.information.wrapper;


import net.bncloud.information.entity.ZcInformationMsg;
import net.bncloud.information.vo.ZcInformationMsgVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 智采消息表
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2021-03-22
 */
public class ZcInformationMsgWrapper extends BaseEntityWrapper<ZcInformationMsg,ZcInformationMsgVo>  {

	public static ZcInformationMsgWrapper build() {
		return new ZcInformationMsgWrapper();
	}

	@Override
	public ZcInformationMsgVo entityVO(ZcInformationMsg entity) {
        ZcInformationMsgVo entityVo = BeanUtil.copy(entity, ZcInformationMsgVo.class);
		return entityVo;
	}



}
