package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.FactoryInfo;
import net.bncloud.delivery.entity.FactoryTransportationDuration;
import net.bncloud.delivery.param.FactoryInfoParam;
import net.bncloud.delivery.param.FactoryTransportationDurationParam;
import net.bncloud.delivery.vo.FactoryTransportationDurationVo;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * 工厂运输时长service 接口
 *
 * @author liyh
 * @since 2021-05-16
 */
public interface FactoryTransportationDurationService extends BaseService<FactoryTransportationDuration> {


    PageImpl<FactoryTransportationDurationVo> selectPageList(IPage<FactoryTransportationDurationVo> page, QueryParam<FactoryTransportationDurationParam> queryParam);

    void batchDeleteTransportationDuration(List<Long> ids);

    void addOrEditTransportationDuration(List<FactoryTransportationDuration> factoryTransportationDurationList);

    List<FactoryInfo> getFactoryInfoList(QueryParam<FactoryInfoParam> queryParam);

    /**
     * 查询运输时长信息
     * @param purchaserCode
     * @param supplierCode
     */
    Integer getTransportationDuration(String purchaserCode, String supplierCode);
}
