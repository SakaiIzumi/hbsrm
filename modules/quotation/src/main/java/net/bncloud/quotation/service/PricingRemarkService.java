package net.bncloud.quotation.service;

import net.bncloud.quotation.entity.PricingRemark;
import net.bncloud.quotation.vo.PricingRemarkVo;
import net.bncloud.quotation.param.PricingRemarkParam;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;

/**
 * <p>
 * 定价说明信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface PricingRemarkService extends BaseService<PricingRemark> {

		/**
         * 自定义分页
         * @param page
         * @param pageParam
         * @return
         */
		IPage<PricingRemark> selectPage(IPage<PricingRemark> page, QueryParam<PricingRemarkParam> pageParam);


}
