package net.bncloud.bis.srm.doc.manager;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.config.OaConfiguration;
import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.constant.DatasourceConstants;
import net.bncloud.bis.model.oa.UfHzhbxxb;
import net.bncloud.bis.model.oa.UfSrmContract;
import net.bncloud.bis.properties.ApplicationProperties;
import net.bncloud.bis.service.UfSrmContractService;
import net.bncloud.bis.srm.doc.client.ISrmDocSynchro;
import net.bncloud.bis.srm.doc.client.SrmDocSynchroDelegate;
import net.bncloud.bis.srm.doc.model.ContractFile;
import net.bncloud.bis.srm.doc.service.ContractFileService;
import net.bncloud.bis.srm.doc.webservice.ArrayOfInt;
import net.bncloud.common.api.R;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.util.ThrowableUtils;
import net.bncloud.service.api.contract.dto.ContractDTO;
import net.bncloud.service.api.contract.dto.FileInfoDTO;
import net.bncloud.service.api.contract.feign.ContractFeignClient;
import net.bncloud.service.api.file.dto.FileInfoDto;
import net.bncloud.service.api.file.dto.MultipartFileDto;
import net.bncloud.service.api.file.feign.FileCenterFeignClient;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Component
//@EnableScheduling
public class ContractFileManager {

    @Resource
    private ISrmDocSynchro srmDocSynchro;

    @Autowired
    private RedissonClient redissonClient;

    @Resource(name = "bisUfSrmContractServiceImpl")
    private UfSrmContractService bisUfSrmContractService;

    @Resource
    private ContractFileService contractFileService;

    @Resource
    private FileCenterFeignClient fileCenterFeignClient;

    @Resource
    private ContractFeignClient contractFeignClient;

    @Resource
    private SupplierFeignClient supplierFeignClient;

    @Resource
    private PurchaserFeignClient purchaserFeignClient;

    @Autowired
    private ApplicationProperties applicationProperties;


    private IPage querySrmContractPage(Page page, Set<Integer> requestIds, Collection<String> docIds,Date modifyDate){

        IPage page1=null;
        try{
            DynamicDataSourceContextHolder.push( DatasourceConstants.MS_OA );
            page1 = bisUfSrmContractService.page(page,
                    Wrappers.<UfSrmContract>lambdaQuery()
                            // 远程 创建合同的时候会 有更新时间
                            .gt(Objects.nonNull(modifyDate),UfSrmContract::getModedatamodifydatetime, DateUtil.format(modifyDate,DateUtil.PATTERN_DATETIME))
                            .and(CollectionUtils.isNotEmpty(requestIds),or -> or.notIn( UfSrmContract::getRequestId, requestIds)
                                    .notIn(CollectionUtils.isNotEmpty(docIds), UfSrmContract::getXgfj, docIds)
                                    .notIn(CollectionUtils.isNotEmpty(docIds), UfSrmContract::getFjsc, docIds)))
                            ;
        }catch (Exception e){
            log.error("同步合同数据出现问题，{}", JSON.toJSONString(e.getMessage()),e);
            throw new RuntimeException("同步合同数据出现问题",e);
        }finally {
            DynamicDataSourceContextHolder.poll();
        }




        return page1;
//        return bisUfSrmContractService.page(page,
//                Wrappers.<UfSrmContract>lambdaQuery()
//                        .notIn(CollectionUtils.isNotEmpty(requestIds), UfSrmContract::getRequestId, requestIds)
//                        .or().notIn(CollectionUtils.isNotEmpty(docIds), UfSrmContract::getXgfj, docIds)
//        );
    }

    private List<Map<String, Object>> getDocFileList(Integer requestId,List<Integer> docIdList){
        ArrayOfInt arrayOfInt = new ArrayOfInt();
        arrayOfInt.setInt(docIdList);
        return srmDocSynchro.getDocFileList(requestId, arrayOfInt);
    }

//    @Scheduled(cron = "0 0/1 * * * ?")
    @DS(DatasourceConstants.BIS)
    public void syncContractFile(){

        RMap<Integer, String> rMap = redissonClient.getMap(BisSyncConstants.CONTRACT_FILE_SYNC_ERP_TASK, JsonJacksonCodec.INSTANCE);
        Long current = 1L;
        Long total = 0L;
        List<UfSrmContract> list = new ArrayList<>();
        do{
            IPage<UfSrmContract> page = querySrmContractPage(new Page<>(current, 100L),null,null,null);
            total = page.getPages();
            list.addAll(page.getRecords());
            current++;
        }while (current < total);

        List<ContractFile> contractFileList = new ArrayList<>();
        for (UfSrmContract contract : list){

            //循环遍历后获取附件
            List<String> ids = Arrays.asList(contract.getXgfj().split(","));

            List<Integer> docIdList = new ArrayList<>();
            //转为integer列表
            CollectionUtils.collect(ids, (Transformer) o -> Integer.valueOf(o.toString()), docIdList);

            //通过合同的requestId获取和附件ids获取列表
            List<Map<String, Object>> docFileList = getDocFileList(contract.getRequestId(),docIdList);
            //遍历这个list里面的map
            for(Map<String, Object> map : docFileList){
                //获取id和文件名
               Integer id = Integer.parseInt(map.get("id")+"");
               String fileName = map.get("fileName")+"";
               //获取文件内容
               String fileContent = map.get("fileContent").toString();
                try {
                    //创建MultipartFile对象，准备上传srm文件服务器
                    MultipartFile multipartFile = new MultipartFileDto("file", fileName, ContentType.APPLICATION_OCTET_STREAM.toString(),decode(fileContent));
                    //开始上传
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
                //完成一次上传文件的遍历
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



    //    @Scheduled(cron = "0 0/1 * * * ?")
    @DS(DatasourceConstants.MS_OA)
    public void syncContractAndContractFile(){
        log.info("==============================");
        log.info("开始同步合同数据");
        log.info("==============================");
        // 获取上一次同步的时间蹉
        Date startDate = new Date();
        RMap<String, Date> syncDataTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
        Date lastSyncDataTime = syncDataTimeMap.getOrDefault(BisSyncConstants.CONTRACT_DATA_SYNC_ERP_TASK, applicationProperties.getSchedulingTask().getDefaultSyncDateTime(BisSyncConstants.CONTRACT_DATA_SYNC_ERP_TASK));
        Long current = 1L;
        Long total = 0L;
        log.info("==============================");
        log.info("开始获取合同数据");
        log.info("==============================");
        List<UfSrmContract> list = new ArrayList<>();
        do{
            IPage<UfSrmContract> page = querySrmContractPage(new Page<>(current, 100L), null,null,lastSyncDataTime);
            total = page.getPages();
            list.addAll(page.getRecords());
            current++;
        }while (current < total);
        log.info("==============================");
        log.info("oa获取合同数据成功");
        log.info("==============================");
        //对从oa表中获取到的List<UfSrmContract> list进行操作，遍历把合同字段设置进dto然后保存
        List<ContractDTO> contractDTOS = new ArrayList<>();
        List<ContractFile> contractFileList = new ArrayList<>();
        int i=0;
        for (UfSrmContract contract : list){

            //todo **************
            //过滤条件
            //contract.getHzflx();//合作方类型
            //contract.getHtlx2();//物料采购框架合同(存货相关)

            if(contract.getHzflx()!=0){//合作方类型 (供应商 0) 不为0需要过滤
                continue;
            }
            if(contract.getHtlx2()!=0){//物料采购框架合同(存货相关) 0 不为0需要过滤
                continue;
            }
            //todo **************

            ContractDTO contractDTO = new ContractDTO();
            contractDTO.setContractCode(contract.getHth());//合同号
            contractDTO.setContractTitle(contract.getBt());//标题
            contractDTO.setEventType(contract.getHtlx2());//事项类型

            Date parse=null;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                parse = formatter.parse(contract.getSqrq());
            } catch (ParseException e) {
                log.error("日期转换出现问题,{}", JSON.toJSONString(contract),e);
                throw new RuntimeException("日期转换出现问题");

            }

            contractDTO.setSignedTime(parse);//申请日期
            contractDTO.setProcessNumber(contract.getLcbh());//流程编号

            contractDTO.setTaxIncludedAmount(contract.getHtje());//合同金额
            contractDTO.setRequestId(contract.getRequestId());
            contractDTO.setProcessStatus(contract.getSrmlczt());//流程状态
            contractDTO.setPartnerType(contract.getHzflx());//合作方类型


            contractDTO.setCustomerName(contract.getWfdwmcTxt());//采购方公司名称/我方单位名称
            contractDTO.setSupplierName(contract.getHzhbTxt());//供应方公司名称/ 对方单位名称
            contractDTO.setCustomerCode( contract.getWfdwmcErpnum());//获取采购方公司的编码
            contractDTO.setSupplierCode(contract.getHzhbErpnum());//获取销售供应商的编码
            //todo *****************

            //调用saas，获取供应商和采购商的编码
           /* R<String> supplierCode = supplierFeignClient.findSupplierByName(contract.getHzhb());
            if(!(supplierCode.isSuccess())){
                log.error("获取供应商编码出问题，{}",JSON.toJSONString(supplierCode));
                throw new RuntimeException("获取供应商编码出问题，{}"+supplierCode);
            }
            String code = supplierCode.getData();
            contractDTO.setSupplierCode(code);

            R<String> customerCode = purchaserFeignClient.findCustomerCode(contract.getWfdwmcTxt());
            if(!(customerCode.isSuccess())){
                log.error("获取采购商编码出问题，{}",JSON.toJSONString(customerCode));
                throw new RuntimeException("获取采购商编码出问题，{}"+customerCode);
            }
            String customerForCode = customerCode.getData();
            contractDTO.setSupplierCode(customerForCode);*/

            //todo *****************
            //字段设置结束，添加到列表
            contractDTOS.add(contractDTO);

            //循环遍历后获取附件
            List<String> ids = new ArrayList<>();
            if(StringUtils.isNotBlank(contract.getXgfj())){
                Collections.addAll(ids,contract.getXgfj().split(","));
            }
            if(StringUtils.isNotBlank(contract.getFjsc())){
                Collections.addAll(ids,contract.getFjsc().split(","));
            }

            List<Integer> docIdList = new ArrayList<>();

            boolean flag=true;
            for (String id : ids) {
                if(id==null||id.equals("")){
                    flag=false;
                }
            }

            //转为integer列表
            if(flag){
                CollectionUtils.collect(ids, (Transformer) o -> Integer.valueOf(o.toString()), docIdList);

                //通过合同的requestId获取和附件ids获取列表
                List<Map<String, Object>> docFileList = getDocFileList(contract.getRequestId(),docIdList);

                List<FileInfoDTO> fileInfoDTOs = new ArrayList<>();

                //遍历这个list里面的map
                for(Map<String, Object> map : docFileList){
                    //获取id和文件名
                    Integer id = Integer.parseInt(map.get("id")+"");
                    String fileName = map.get("fileName")+"";
                    //获取文件内容
                    String fileContent = map.get("fileContent").toString();
                    try {
                        //创建MultipartFile对象，准备上传srm文件服务器
                        MultipartFile multipartFile = new MultipartFileDto("file", fileName, ContentType.APPLICATION_OCTET_STREAM.toString(),decode(fileContent));
                        //开始上传
                        R<List<FileInfoDto>> upload = fileCenterFeignClient.upload(new MultipartFile[]{multipartFile});

                        List<FileInfoDto> data = upload.getData();
                        if(CollectionUtils.isNotEmpty(data)){

                            //上传的同时存到fileinfo里，准备保存远程调用
                            FileInfoDTO fileInfoDTO = new FileInfoDTO();
                            fileInfoDTO.setId(data.get(0).getId());
                            fileInfoDTO.setOriginalFilename(data.get(0).getOriginalFilename());
                            fileInfoDTO.setFilename(data.get(0).getFilename());
                            fileInfoDTO.setExtension(data.get(0).getExtension());
                            fileInfoDTO.setContentType(data.get(0).getContentType());
                            fileInfoDTO.setSize(data.get(0).getSize());
                            fileInfoDTO.setPath(data.get(0).getPath());
                            fileInfoDTO.setUrl(data.get(0).getUrl());
                            fileInfoDTO.setRequestId(contract.getRequestId());
                            fileInfoDTOs.add(fileInfoDTO);



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
                    //完成一次上传文件的遍历
                }
                contractDTOS.get(i).setAttachmentList(fileInfoDTOs);

            }
            //循环次数，方便上面设置合同附件保存
            i++;
        }
        log.info("==============================");
        log.info("开始同步数据到合同模块");
        log.info("同步的数据为：{}",JSON.toJSONString(contractDTOS));
        log.info("==============================");
        R<Object> syncdata = contractFeignClient.syncdata(contractDTOS);
        if(! syncdata.isSuccess()){
            log.error("同步合同数据出问题：{}", syncdata.getMsg());
            throw new RuntimeException("同步合同数据出问题");
        }
        syncDataTimeMap.fastPut( BisSyncConstants.CONTRACT_DATA_SYNC_ERP_TASK,startDate );
    }



}
