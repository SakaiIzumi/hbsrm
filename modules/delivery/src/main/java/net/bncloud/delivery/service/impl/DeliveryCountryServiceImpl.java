package net.bncloud.delivery.service.impl;

import net.bncloud.delivery.entity.DeliveryCountry;
import net.bncloud.delivery.mapper.DeliveryCountryMapper;
import net.bncloud.delivery.service.DeliveryCountryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.delivery.param.DeliveryCountryParam;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;

/**
 * <p>
 * 国家信息表 服务实现类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Service
public class DeliveryCountryServiceImpl extends BaseServiceImpl<DeliveryCountryMapper, DeliveryCountry> implements DeliveryCountryService {

    @Override
    public IPage<DeliveryCountry> selectPage(IPage<DeliveryCountry> page, QueryParam<DeliveryCountryParam> queryParam) {
        // 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
        //notice.setTenantId(SecureUtil.getTenantId());
        return page.setRecords(baseMapper.selectListPage(page, queryParam));
    }
}
