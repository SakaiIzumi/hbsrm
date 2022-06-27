package net.bncloud.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.SupplyDemandBalance;
import net.bncloud.delivery.param.SupplyDemandBalanceParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * @author ddh
 * @since 2022/4/8
 * @description 供需平衡报表
 */
@Mapper
public interface SupplyDemandBalanceMapper extends BaseMapper<SupplyDemandBalance> {

    List<SupplyDemandBalance> selectReportPage(IPage<SupplyDemandBalance> page,@Param("queryParam") QueryParam<SupplyDemandBalanceParam> queryParam);

    @Update("truncate table t_supply_demand_balance")
    void cleanTable();
}