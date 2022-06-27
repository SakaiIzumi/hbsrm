package net.bncloud.delivery.mapper;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryCountry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.delivery.param.DeliveryCountryParam;

import java.util.List;

/**
 * <p>
 * 国家信息表 Mapper 接口
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
public interface DeliveryCountryMapper extends BaseMapper<DeliveryCountry> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author huangtao
     * @since 2021-03-17
     */
    List<DeliveryCountry> selectListPage(IPage page, QueryParam<DeliveryCountryParam> queryParam);

}
