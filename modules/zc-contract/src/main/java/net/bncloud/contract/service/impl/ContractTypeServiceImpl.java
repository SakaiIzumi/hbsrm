package net.bncloud.contract.service.impl;

import net.bncloud.contract.entity.ContractType;
import net.bncloud.contract.mapper.ContractTypeMapper;
import net.bncloud.contract.service.ContractTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;

/**
 * <p>
 * 合同类型信息表 服务实现类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
@Service
public class ContractTypeServiceImpl extends BaseServiceImpl<ContractTypeMapper, ContractType> implements ContractTypeService {

}
