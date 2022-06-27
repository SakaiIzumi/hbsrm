package net.bncloud.delivery.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.exception.Asserts;
import net.bncloud.delivery.entity.DataConfig;
import net.bncloud.delivery.enums.DataConfigCodeEnum;
import net.bncloud.delivery.mapper.DataConfigMapper;
import net.bncloud.delivery.service.DataConfigService;
import org.springframework.stereotype.Service;

/**
 * @author ddh
 * @description
 * @since 2022/5/19
 */
@Service
public class DataConfigServiceImpl extends BaseServiceImpl<DataConfigMapper, DataConfig> implements DataConfigService {
    @Override
    public void updateCurrentMrpPlanOrderComputerNo(String computerNo) {
        this.update( new DataConfig().setValue( computerNo), Wrappers.<DataConfig>lambdaUpdate().eq( DataConfig::getCode,DataConfigCodeEnum.currentPlanOrderComputerNo.name() ) );
    }

    @Override
    public void updatePreMrpPlanOrderComputerNo(String computerNo) {
        if(StrUtil.isNotBlank( computerNo)) {
            this.update( new DataConfig().setValue( computerNo), Wrappers.<DataConfig>lambdaUpdate().eq( DataConfig::getCode,DataConfigCodeEnum.prePlanOrderComputerNo.name() ) );
        }

    }

    @Override
    public String getMrpPlanOrderComputerNo() {
        DataConfig planOrderComputerNoData = this.getOne(Wrappers.<DataConfig>lambdaQuery().eq(DataConfig::getCode, DataConfigCodeEnum.currentPlanOrderComputerNo.name() ));
        Asserts.notNull( planOrderComputerNoData,"查询mrp最新运算编号数据不存在！" );
        return planOrderComputerNoData.getValue();
    }
}
