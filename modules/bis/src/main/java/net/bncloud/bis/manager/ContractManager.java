package net.bncloud.bis.manager;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.constant.DatasourceConstants;
import net.bncloud.bis.dao.UfSrmContractDao;
import net.bncloud.bis.model.oa.UfSrmContract;
import net.bncloud.bis.service.UfSrmContractService;
import net.bncloud.bis.srm.doc.client.ISrmDocSynchro;
import net.bncloud.bis.srm.doc.client.SrmDocSynchroDelegate;
import net.bncloud.bis.srm.doc.model.ContractFile;
import net.bncloud.bis.srm.doc.service.ContractFileService;
import net.bncloud.bis.srm.doc.webservice.ArrayOfInt;
import net.bncloud.bis.srm.financial.enums.FinancialBillTypeEnum;
//import net.bncloud.bis.srm.financial.model.erp.SettlementPool;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.util.ThrowableUtils;
import net.bncloud.msk3cloud.client.K3CloudApiClient;
import net.bncloud.msk3cloud.constant.formid.FinancialConstants;
import net.bncloud.msk3cloud.core.condition.QueryCondition;
import net.bncloud.msk3cloud.kingdee.K3cloudRemoteService;
import net.bncloud.msk3cloud.util.FieldKeyAnoUtils;
import net.bncloud.service.api.contract.feign.ContractFeignClient;
import net.bncloud.service.api.file.dto.*;
import net.bncloud.service.api.file.feign.FileCenterFeignClient;
import net.bncloud.service.api.file.feign.FinancialFeignClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.http.entity.ContentType;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

import static net.bncloud.bis.srm.financial.enums.SettlementPoolDocumentTypeEnum.YFD02_SYS;
import static net.bncloud.bis.srm.financial.enums.SettlementPoolDocumentTypeEnum.valueOf;

@Slf4j
@Component
@AllArgsConstructor
@EnableScheduling
public class ContractManager {

    private ISrmDocSynchro srmDocSynchro;

    @Autowired
    private RedissonClient redissonClient;

    @Resource(name = "bisUfSrmContractServiceImpl")
    private UfSrmContractService bisUfSrmContractService;

    @Resource
    private ContractFileService contractFileService;

    @Resource
    private FileCenterFeignClient fileCenterFeignClient;

    public ContractManager(){
        srmDocSynchro =  new SrmDocSynchroDelegate();
    }














    private IPage querySrmContractPage(Page page, Set<Integer> requestIds, Collection<String> docIds){
        return bisUfSrmContractService.page(page,
                Wrappers.<UfSrmContract>lambdaQuery()
                        .notIn(CollectionUtils.isNotEmpty(requestIds), UfSrmContract::getRequestId, requestIds)
                        .or().notIn(CollectionUtils.isNotEmpty(docIds), UfSrmContract::getXgfj, docIds)
        );
    }




    private List<Map<String, Object>> getDocFileList(Integer requestId,List<Integer> docIdList){
        ArrayOfInt arrayOfInt = new ArrayOfInt();
        arrayOfInt.setInt(docIdList);
        return srmDocSynchro.getDocFileList(requestId, arrayOfInt);
    }






    //    @Scheduled(cron = "0 0/1 * * * ?")
    @DS(DatasourceConstants.BIS)
    public void syncContract(){

        RMap<Integer, String> rMap = redissonClient.getMap(BisSyncConstants.CONTRACT_FILE_SYNC_ERP_TASK, JsonJacksonCodec.INSTANCE);
//        rMap.clear();
        Long current = 1L;
        Long total = 0L;
        List<UfSrmContract> list = new ArrayList<>();
        do{
            IPage<UfSrmContract> page = querySrmContractPage(new Page<>(current, 100L), rMap.keySet(), rMap.readAllValues());
            total = page.getPages();
            list.addAll(page.getRecords());
            current++;
        }while (current < total);

        List<ContractFile> contractFileList = new ArrayList<>();
        for (UfSrmContract contract : list){
            List<String> ids = Arrays.asList(contract.getXgfj().split(","));
            List<Integer> docIdList = new ArrayList<>();
            CollectionUtils.collect(ids, (Transformer) o -> Integer.valueOf(o.toString()), docIdList);
            List<Map<String, Object>> docFileList = getDocFileList(contract.getRequestId(),docIdList);
            for(Map<String, Object> map : docFileList){
                Integer id = Integer.parseInt(map.get("id")+"");
                String fileName = map.get("fileName")+"";
                String fileContent = map.get("fileContent").toString();
                try {
                    MultipartFile multipartFile = new MultipartFileDto("file", fileName, ContentType.APPLICATION_OCTET_STREAM.toString(),decode(fileContent));
                    R<List<FileInfoDto>> upload = fileCenterFeignClient.upload(new MultipartFile[]{multipartFile});
                    List<FileInfoDto> data = upload.getData();
                    if(CollectionUtils.isNotEmpty(data)){
                        ContractFile contractFile = new ContractFile();
                        contractFile.setContractId(contract.getId());
                        contractFile.setDocFileId(id);
                        contractFile.setDocFileName(fileName);
                        contractFile.setRequestId(contract.getRequestId());
                        contractFile.setFileId(data.get(0).getId());
                        contractFileList.add(contractFile);
                    }
                } catch (Exception e) {
                    log.warn("上传附件异常：{}", ThrowableUtils.toString(e));
                }
            }
            rMap.putIfAbsent(contract.getRequestId(),contract.getXgfj());
        }
        //保存数据
        try {
            if(CollectionUtils.isNotEmpty(contractFileList)){
                contractFileService.saveBatch(contractFileList);
            }
        } catch (Exception e){
            log.error("[ContractFile] batch insert error!\n{} ",ThrowableUtils.toString(e));
            return;
        }
    }

    private byte[] decode(String str){
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(str.trim());
    }










}
