package net.bncloud.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.FactoryTransportationDuration;
import net.bncloud.delivery.param.FactoryTransportationDurationParam;
import net.bncloud.delivery.vo.FactoryTransportationDurationVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liyh
 * @description 工厂运输时长mapper
 * @since 2022/5/16
 */
public interface FactoryTransportationDurationMapper extends BaseMapper<FactoryTransportationDuration> {

    List<FactoryTransportationDurationVo> selectListPage(IPage<FactoryTransportationDurationVo> page, @Param("queryParam") QueryParam<FactoryTransportationDurationParam> queryParam);

}
