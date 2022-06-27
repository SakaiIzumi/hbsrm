package net.bncloud.oem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.oem.domain.entity.ReceivingAddress;
import net.bncloud.oem.domain.param.ReceivingAddressParam;

import java.util.List;

/**
 * @author ddh
 * @since 2022/4/24
 * @description
 */
public interface ReceivingAddressMapper extends BaseMapper<ReceivingAddress> {

    List<ReceivingAddress> selectListPage(IPage<ReceivingAddress> page, QueryParam<ReceivingAddressParam> pageParam);
}