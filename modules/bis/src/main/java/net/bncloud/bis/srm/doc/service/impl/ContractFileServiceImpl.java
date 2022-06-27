package net.bncloud.bis.srm.doc.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.DatasourceConstants;
import net.bncloud.bis.srm.doc.dao.ContractDocInfoDao;
import net.bncloud.bis.srm.doc.model.ContractFile;
import net.bncloud.bis.srm.doc.service.ContractFileService;
import org.springframework.stereotype.Service;

@DS( DatasourceConstants.BIS )
@Service
@Slf4j
public class ContractFileServiceImpl extends ServiceImpl<ContractDocInfoDao, ContractFile> implements ContractFileService {

}
