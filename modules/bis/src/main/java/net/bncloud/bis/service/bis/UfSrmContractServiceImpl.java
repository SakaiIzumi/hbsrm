package net.bncloud.bis.service.bis;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.DatasourceConstants;
import net.bncloud.bis.dao.UfSrmContractDao;
import net.bncloud.bis.model.oa.UfSrmContract;
import net.bncloud.bis.service.UfSrmContractService;
import org.springframework.stereotype.Service;

@DS( DatasourceConstants.MS_OA )
@Service("bisUfSrmContractServiceImpl")
@Slf4j
public class UfSrmContractServiceImpl extends ServiceImpl<UfSrmContractDao, UfSrmContract> implements UfSrmContractService {



}
