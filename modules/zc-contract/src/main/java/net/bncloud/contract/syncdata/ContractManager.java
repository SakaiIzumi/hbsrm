package net.bncloud.contract.syncdata;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.contract.entity.AttachmentRel;
import net.bncloud.contract.entity.Contract;
import net.bncloud.contract.entity.FileInfo;
import net.bncloud.contract.enums.ContractCfgParam;
import net.bncloud.contract.param.ContractSaveParam;
import net.bncloud.contract.service.ContractService;
import net.bncloud.service.api.contract.dto.ContractDTO;
import net.bncloud.service.api.contract.dto.FileInfoDTO;
import net.bncloud.service.api.contract.feign.ContractFeignClient;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
public class ContractManager implements ContractFeignClient {
    @Resource
    private CfgParamResourceFeignClient cfgParamResourceFeignClient;

    @Autowired
    private ContractService contractService;

    @Resource
    private PurchaserFeignClient purchaserFeignClient;


    @Override
    public R<Object> syncdata(List<ContractDTO> contractDTOList) {
        log.info("====================");
        log.info("进入合同同步方法");
        log.info("数据：{}",JSON.toJSONString(contractDTOList));
        log.info("====================");
        try{
            //去重，更新
            log.info("====================");
            log.info("去重更新");
            log.info("====================");
            List<ContractDTO> removeList = new ArrayList<>();
            for (int i = 0; i < contractDTOList.size(); i++) {

                //原来是ContractCode，发现不唯一，改用requestId
//                Contract contract=contractService.getByContractCode(contractDTOList.get(i).getContractCode());

                Contract contract=contractService.getByContractCode(contractDTOList.get(i).getRequestId());
                //不为空的时候就是已经有了这条记录，应该是更新
                if(contract!=null){

                    //不改变状态更新
                    String contractStatusCode = contract.getContractStatusCode();

                    contractDTOList.get(i).setId(contract.getId());
                    contractDTOList.get(i).setContractStatusCode(contractStatusCode);
                    removeList.add(  contractDTOList.get(i)  );
                }
            }
            contractDTOList.removeAll(removeList);//移除


            //循环更新
            log.info("====================");
            log.info("去重后的list：");
            log.info("数据：{}",JSON.toJSONString(contractDTOList));
            log.info("removeList：{}",JSON.toJSONString(removeList));
            log.info("====================");
            for (ContractDTO contractDTO : removeList) {

                //更新前查看状态，草稿状态才更新？？？

                ContractSaveParam copy = BeanUtil.copy(contractDTO, ContractSaveParam.class);
                List<FileInfoDTO> attachmentList = contractDTO.getAttachmentList();

                List<FileInfo> fileInfoList = new ArrayList<>();
                for (FileInfoDTO fileInfoDTO : attachmentList) {
                    FileInfo fileInfo = FileInfo.builder()
                            .id(fileInfoDTO.getId())
                            .originalFilename(fileInfoDTO.getOriginalFilename())
                            .url(fileInfoDTO.getUrl())
                            .requestId(fileInfoDTO.getRequestId())
                            .build();
                    fileInfoList.add(fileInfo);

                }
                copy.setAttachmentList(fileInfoList);
                Date signedTime = copy.getSignedTime();
                copy.setCreatedDate(signedTime);
                copy.setSignedTime(null);


                log.info("====================");
                log.info("更新");
                log.info("数据：{}",JSON.toJSONString(copy));
                log.info("====================");
                contractService.updateContract(copy);
            }


            log.info("====================");
            log.info("获取开关");
            log.info("====================");
            //开关控制是否再次获取协同配置
            boolean conf=true;
            boolean confSaveParam=false;//默认关闭，不同步

            log.info("====================");
            log.info("开始保存");
            log.info("====================");
            //剩下的就是保存,去重后不为空就保存
            if(  !  (contractDTOList.isEmpty())  ){
                for (ContractDTO contractDTO : contractDTOList) {

                    //todo orgid ***************
                    //进行保存操作之前先通过供应商编码和采购商编码查询orgId，并且进行设置
                    //如果没有orgId，那么就是没有建立档案的供应商，跳过
                    OrgIdQuery orgIdQuery = new OrgIdQuery();
                    orgIdQuery.setSupplierCode(contractDTO.getSupplierCode());
                    orgIdQuery.setPurchaseCode(contractDTO.getCustomerCode());
                    R<OrgIdDTO> orgIdDTO = purchaserFeignClient.info(orgIdQuery);

                    if(   !(orgIdDTO.isSuccess()) ||   orgIdDTO.getData()==null||orgIdDTO.getData().getOrgId()==null ){
                        log.warn("此合同的供应商还没有和采购商建立档案,{}",JSON.toJSONString(contractDTO));
                        continue;
                    }else{
                        //设置orgId
                        contractDTO.setOrgId(orgIdDTO.getData().getOrgId());
                        //设置客户名字customName
                        contractDTO.setCustomerName(orgIdDTO.getData().getPurchaseName());
                        contractDTO.setSupplierName(orgIdDTO.getData().getSupplierame());
                    }
                    //todo orgid ***************

                    ContractSaveParam contractSaveParam = new ContractSaveParam();
                    BeanUtils.copyProperties(contractDTO,contractSaveParam );
                    List<FileInfoDTO> attachmentList = contractDTO.getAttachmentList();
                    List<FileInfo> fileInfoList = new ArrayList<>();
                    for (FileInfoDTO fileInfoDTO : attachmentList) {
                        FileInfo fileInfo = FileInfo.builder()
                                .id(fileInfoDTO.getId())
                                .originalFilename(fileInfoDTO.getOriginalFilename())
                                .filename(fileInfoDTO.getFilename())
                                .extension(fileInfoDTO.getExtension())
                                .contentType(fileInfoDTO.getContentType())
                                .size(fileInfoDTO.getSize())
                                .path(fileInfoDTO.getPath())
                                .url(fileInfoDTO.getUrl())
                                .requestId(fileInfoDTO.getRequestId())
                                .build();
                        fileInfoList.add(fileInfo);

                    }
                    contractSaveParam.setAttachmentList(fileInfoList);

                    Date signedTime = contractSaveParam.getSignedTime();
                    contractSaveParam.setContractCreateTime(signedTime);
                    //contractSaveParam.setCreatedDate(signedTime);
                    contractSaveParam.setSignedTime(null);

                    //保存之前获取合同开关
                    if(conf){
                        R<List<CfgParamDTO>> listByCode = cfgParamResourceFeignClient.findListByCode(ContractCfgParam.CONTRACT_AUTO_SEND.getCode());
                        if (!(listByCode.isSuccess())) {
                            throw new RuntimeException("获取同步配置出现异常");
                        }

                        //获取data并且获取CfgParamEntity
                        List<CfgParamDTO> cfgParamDTOList = listByCode.getData();
                        CfgParamDTO paramEntity = cfgParamDTOList.stream().filter(cfgParamDTO -> cfgParamDTO.getOrgId().equals(orgIdDTO.getData().getOrgId())).findFirst().orElse(null);
                        if (paramEntity != null) {
                            //协同配置：自动发送送货计划
                            if ("true".equals(paramEntity.getValue())) {
                                //配置为true
                                //开关打开
                                conf=false;
                                confSaveParam=true;//同步打开

                            }else{
                                //为false，已经获取开关，再次获取关闭
                                conf=false;

                            }
                        }
                    }

                    log.info("====================");
                    log.info("保存");
                    log.info("数据：{}",JSON.toJSONString(contractSaveParam));
                    log.info("====================");
                    R r = contractService.saveContract(contractSaveParam,confSaveParam);
                }
            }

        }catch (Exception e){
            log.error("合同服务保存合同数据出现问题,{}",JSON.toJSONString(e),e);
            R.fail(e.getMessage());
//            throw new RuntimeException("合同服务保存合同数据出现问题"+e.getMessage());
        }

        return R.success();
    }
}
