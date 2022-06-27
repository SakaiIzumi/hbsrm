package net.bncloud.contract.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.contract.entity.Contract;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bncloud.contract.param.ContractQueryParam;

import java.util.List;

/**
 * <p>
 * 合同信息表 Mapper 接口
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
public interface ContractMapper extends BaseMapper<Contract> {

    List<Contract> selectListPage(IPage<Contract> page, QueryParam<ContractQueryParam> queryParam);

}
