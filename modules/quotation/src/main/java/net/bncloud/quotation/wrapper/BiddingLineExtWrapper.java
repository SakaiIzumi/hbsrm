package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.BiddingLineExt;
import net.bncloud.quotation.vo.BiddingLineExtVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 招标行信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class BiddingLineExtWrapper extends BaseEntityWrapper<BiddingLineExt,BiddingLineExtVo>  {

	public static BiddingLineExtWrapper build() {
		return new BiddingLineExtWrapper();
	}

	@Override
	public BiddingLineExtVo entityVO(BiddingLineExt entity) {
        BiddingLineExtVo entityVo = BeanUtil.copy(entity, BiddingLineExtVo.class);
		return entityVo;
	}



}
