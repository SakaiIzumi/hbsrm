package net.bncloud.delivery.service;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryCountry;
import net.bncloud.delivery.param.DeliveryCountryParam;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 国家信息表 服务类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
public interface DeliveryCountryService extends BaseService<DeliveryCountry> {

		/**
         * 自定义分页
         * @param page
         * @param queryParam
         * @return
         */
		IPage<DeliveryCountry> selectPage(IPage<DeliveryCountry> page, QueryParam<DeliveryCountryParam> queryParam);


}
