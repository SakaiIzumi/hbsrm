package net.bncloud.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.FactoryWorkday;
import net.bncloud.delivery.param.FactoryWorkdayParam;
import net.bncloud.delivery.vo.FactoryWorkdayVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liyh
 * @description 工厂工作日mapper
 * @since 2022/5/16
 */
public interface FactoryWorkdayMapper extends BaseMapper<FactoryWorkday> {

    IPage<FactoryWorkday> selectListPage(IPage<FactoryWorkday> page, @Param("pageParam") QueryParam<FactoryWorkdayParam> queryParam);

    void deleteAllWorkDayFromFactoryId(@Param("ids")List<Long> ids);

    void deleteAllByWorkdayIds(@Param("ids")List<Long> ids);

    IPage<FactoryWorkdayVo> supplierPage(IPage<FactoryWorkday> page,  @Param("pageParam")QueryParam<FactoryWorkdayParam> queryParam);
}
