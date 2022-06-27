package net.bncloud.bis.srm.financial.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.DatasourceConstants;
import net.bncloud.bis.srm.financial.dao.PayableChargesDocumentDao;
import net.bncloud.bis.srm.financial.model.vo.PayableChargesDocument;
import net.bncloud.bis.srm.financial.service.PayableChargesDocumentService;
import org.springframework.stereotype.Service;

@DS( DatasourceConstants.BIS )
@Service
@Slf4j
public class PayableChargesDocumentServiceImpl extends ServiceImpl<PayableChargesDocumentDao, PayableChargesDocument> implements PayableChargesDocumentService {

}
