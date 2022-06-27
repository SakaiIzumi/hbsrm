package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.RestateSupplier;
import net.bncloud.quotation.param.RestateSupplierParam;
import net.bncloud.quotation.vo.RestateSupplierVo;

/**
 * <p>
 * 询价重报供应商邀请信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-08
 */
public interface IRestateSupplierService extends BaseService<RestateSupplier> {

		/**
         * 自定义分页
         * @param page
         * @param pageParam
         * @return
         */
		IPage<RestateSupplierVo> selectPage(IPage<RestateSupplierVo> page, QueryParam<RestateSupplierParam> pageParam);


}
