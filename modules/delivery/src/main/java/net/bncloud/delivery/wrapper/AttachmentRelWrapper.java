package net.bncloud.delivery.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.entity.AttachmentRel;
import net.bncloud.delivery.vo.AttachmentRelVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 订单合同与附件关联关系表
 * </p>
 *包装类,返回视图层所需的字段
 * @author huangtao
 * @since 2021-03-13
 */
public class AttachmentRelWrapper extends BaseEntityWrapper<AttachmentRel, AttachmentRelVo>  {

	public static AttachmentRelWrapper build() {
		return new AttachmentRelWrapper();
	}

	@Override
	public AttachmentRelVo entityVO(AttachmentRel entity) {
        AttachmentRelVo entityVo = BeanUtil.copy(entity, AttachmentRelVo.class);
		return entityVo;
	}



}
