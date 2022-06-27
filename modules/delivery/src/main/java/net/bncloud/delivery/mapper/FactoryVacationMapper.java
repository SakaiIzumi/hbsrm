package net.bncloud.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.FactoryVacation;
import net.bncloud.delivery.entity.FactoryWorkday;
import net.bncloud.delivery.param.FactoryVacationParam;
import net.bncloud.delivery.param.FactoryWorkdayParam;
import net.bncloud.delivery.vo.FactoryWorkdayVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author liyh
 * @description 工厂假期mapper
 * @since 2022/5/16
 */
public interface FactoryVacationMapper extends BaseMapper<FactoryVacation> {

    IPage<FactoryVacation> selectListPage(IPage<FactoryVacation> page, @Param("queryParam") QueryParam<FactoryVacationParam> queryParam);

    void deleteAllVacationFromFactoryId(@Param("ids")List<Long> ids);

    void deleteInFactoryId(@Param("factoryIdList")List<Long> factoryIdList);

    void deleteAddManuallyInFactoryId(@Param("factoryIdList")List<Long> factoryIdList);

    void deleteByVacationDate(@Param("id")Long id);

    void deleteByVacationIds(@Param("ids")List<Long> ids);

    List<FactoryVacation> listVacationByFactoryIdAndDateList(@Param("factoryId")Long factoryId, @Param("dateList")List<Date> dateList);
}
