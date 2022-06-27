package net.bncloud.contract.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.security.BncUserDetails;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.service.base.domain.HandlerMsgParam;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.contract.annotation.OperationLog;
import net.bncloud.contract.entity.AttachmentRel;
import net.bncloud.contract.entity.Contract;
import net.bncloud.contract.entity.ContractHistoryRel;
import net.bncloud.contract.entity.ContractOperationLog;
import net.bncloud.contract.entity.ContractType;
import net.bncloud.contract.entity.FileInfo;
import net.bncloud.contract.enums.ContractOperateType;
import net.bncloud.contract.enums.ContractStatus;
import net.bncloud.contract.enums.ContractStatusOperateRel;
import net.bncloud.contract.event.ContractConfirmEvent;
import net.bncloud.contract.event.ContractInvalidEvent;
import net.bncloud.contract.event.ContractRejectEvent;
import net.bncloud.contract.event.ContractSendEvent;
import net.bncloud.contract.event.ContractWithdrawEvent;
import net.bncloud.contract.feign.FileServiceFeignClient;
import net.bncloud.contract.feign.InformationServiceFeignClient;
import net.bncloud.contract.mapper.ContractMapper;
import net.bncloud.contract.param.ContractQueryParam;
import net.bncloud.contract.param.ContractSaveParam;
import net.bncloud.contract.service.AttachmentRelService;
import net.bncloud.contract.service.ContractHistoryRelService;
import net.bncloud.contract.service.ContractOperationLogService;
import net.bncloud.contract.service.ContractService;
import net.bncloud.contract.service.ContractTypeService;
import net.bncloud.contract.vo.ContractStatisticsVo;
import net.bncloud.contract.vo.ContractVo;
import net.bncloud.contract.vo.event.ContractEvent;
import net.bncloud.enums.EventCode;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同信息表 服务实现类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
@Service
@Slf4j
public class ContractServiceImpl extends BaseServiceImpl<ContractMapper, Contract> implements ContractService {


    @Autowired
    private AttachmentRelService attachmentRelService;

    @Autowired
    private ContractOperationLogService operationLogService;

    @Resource
    private FileServiceFeignClient fileServiceFeignClient;

    @Autowired
    private DefaultEventPublisher defaultEventPublisher;

    @Autowired
    private ContractHistoryRelService contractHistoryRelService;

    @Autowired
    private ContractTypeService contractTypeService;
    @Autowired
    private InformationServiceFeignClient informationServiceFeignClient;
    /**
     * 保存合同
     *
     * @param contract
     */
    @Override
    @Transactional
    public R<ContractSaveParam> saveContract(ContractSaveParam contract) {
        contract.setId(null);
        if(StringUtil.isBlank(contract.getContractStatusCode())){
            contract.setContractStatusCode(ContractStatus.DRAFT.getCode());
        }
        if(SecurityUtils.getLoginInfo().isPresent()){
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            contract.setCreatedByName(loginInfo.getName());
        }
        save(contract);
        List<FileInfo> attachmentList = contract.getAttachmentList();
        /*if(CollectionUtil.isEmpty(attachmentList)){
            return R.fail("请上传附件");
        }*/
        List<AttachmentRel> attachmentRelList = attachmentRelService
                .buildAttachmentRelList(contract.getId(),attachmentList);
        attachmentRelService.saveBatch(attachmentRelList);

        return R.data(contract);
    }

    /**
     * 更新合同
     *
     * @param contract
     */
    @Override
    @Transactional
    public R<ContractSaveParam> updateContract(ContractSaveParam contract) {
        updateById(contract);
        List<FileInfo> attachmentList = contract.getAttachmentList();
        /*if(CollectionUtil.isEmpty(attachmentList)){
            return R.fail("请上传附件");
        }*/
        List<AttachmentRel> attachmentRelList = attachmentRelService
                .buildAttachmentRelList(contract.getId(),attachmentList);
        AttachmentRel attachmentRel = AttachmentRel.builder().businessFormId(contract.getId()).build();
        QueryWrapper<AttachmentRel> queryWrapper = Condition.getQueryWrapper(attachmentRel);
        attachmentRelService.remove(queryWrapper);
        attachmentRelService.saveBatch(attachmentRelList);

        return R.success();
    }

    /**
     * 获取合同数量统计信息
     * @return 合同数量统计信息
     */
    @Override
    public ContractStatisticsVo getStatisticsInfo() {
        Contract contract = Contract.builder()
                .contractStatusCode(ContractStatus.TO_BE_ANSWERED.getCode()).build();
        SecurityUtils.getCurrentSupplier().ifPresent(supplier -> contract.setSupplierCode(supplier.getSupplierCode()));
        QueryWrapper<Contract> queryWrapper = Condition.getQueryWrapper(contract);
        int toBeConfirmedNum = count(queryWrapper);

        contract.setContractStatusCode(ContractStatus.REJECTED.getCode());
        int rejectedNum = count(Condition.getQueryWrapper(contract));

        contract.setContractStatusCode(ContractStatus.ABNORMAL.getCode());
        int anomalousNum = count(Condition.getQueryWrapper(contract));

        contract.setContractStatusCode(ContractStatus.VALID.getCode());
        Date date = null;
        try {
            date = DateUtil.parse(DateUtil.formatDate(new Date()), DateUtil.PATTERN_DATE);
        } catch (ParseException e) {
            log.error("日期转化异常",e);
        }
        LambdaQueryWrapper<Contract> wrapper = Condition.getQueryWrapper(contract).lambda()
                .le(Contract::getExpiryDate, DateUtil.plusDays(date,30));
        int nearExpiryNum = count(wrapper);


        return ContractStatisticsVo.builder().toBeConfirmedNum(toBeConfirmedNum)
                .rejectedNum(rejectedNum)
                .anomalousNum(anomalousNum)
                .nearExpiryNum(nearExpiryNum).build();
    }

    /**
     * 发送合同
     * @param contract
     * @return
     */
    @Override
    @Transactional
    public boolean send(ContractSaveParam contract) {
        if(contract.getId() == null){
            saveContract(contract);
        }else{
            updateContract(contract);
        }

        //修改合同单状态
        update(Wrappers.<Contract>update().lambda()
                .set(Contract::getContractStatusCode,ContractStatus.TO_BE_ANSWERED.getCode())
                .eq(Contract::getId,contract.getId()));

        //获取当前登录信息
        Optional<BncUserDetails> userDetailsOptional = SecurityUtils.getCurrentUser();
        if(userDetailsOptional.isPresent()){
            LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
            //记录操作日志
            ContractOperationLog operationLog = ContractOperationLog.builder()
                    .contractId(contract.getId())
                    .operateContent(ContractOperateType.SEND.getName())
                    .operatorNo(loginInfo.getId())
                    .operatorName(loginInfo.getName()).build();
            operationLogService.save(operationLog);


            //发送消息通知，创建钉钉待办
            Contract currentContract = getById(contract.getId());
            ContractEvent contractEvent = BeanUtil.copy(contract, ContractEvent.class);
            log.info("合同发送事件,合同数据{}", JSON.toJSONString(contractEvent));
            contractEvent.setContractId(contract.getId());
            contractEvent.setBusinessId(contract.getId());
            ContractSendEvent contractSendEvent = new ContractSendEvent(this, loginInfo, contractEvent,contractEvent.getSupplierCode(),contractEvent.getSupplierName());
            defaultEventPublisher.publishEvent(contractSendEvent);
        }else{
            log.warn("获取用户登录信息失败");
        }

        return true;
    }

    /**
     * 撤回合同
     *
     * @param contractId 合同ID
     * @return
     */
    @Override
    @Transactional
    @OperationLog(code = "contract_withdraw",content = "撤回")
    public boolean withdraw(String contractId) {
        //修改合同状态
        update(Wrappers.<Contract>update().lambda()
                .set(Contract::getContractStatusCode,ContractStatus.WITHDRAWN.getCode())
                .eq(Contract::getId,contractId));

        //发送消息通知
        if(SecurityUtils.getLoginInfo().isPresent()){
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            Contract contract = getById(contractId);
            ContractEvent contractEvent = BeanUtil.copy(contract, ContractEvent.class);
            contractEvent.setContractId(contract.getId());
            contractEvent.setBusinessId(contract.getId());
            defaultEventPublisher.publishEvent(new ContractWithdrawEvent(this,loginInfo,contractEvent,contractEvent.getSupplierCode(),contractEvent.getSupplierName()));
        }
        return true;
    }

    /**
     * 重新签约
     * @param contractId 合同ID
     * @return
     */
    @Override
    public boolean resign(String contractId) {
        //修改合同状态
        update(Wrappers.<Contract>update().lambda()
                .set(Contract::getContractStatusCode,ContractStatus.TO_BE_SIGNED_ONLINE.getCode())
                .eq(Contract::getId,contractId));
        return true;
    }

    /**
     * 复制生成新合同
     *
     * @param historyContractId 历史合同ID
     * @param contract 合同数据
     * @return 合同
     */
    @Override
    public Contract copy(Long historyContractId, ContractSaveParam contract) {
        //更新中
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
        contract.setContractStatusCode(ContractStatus.UPDATING.getCode());

        saveContract(contract);
        //将历史合同状态，变更为失效
        invalidHistoryContract(historyContractId);
        
        List<ContractHistoryRel> relList = new ArrayList<>();
        //https://gitee.com/baomidou/mybatis-plus/issues/IRLRS
        LambdaQueryWrapper<ContractHistoryRel> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ContractHistoryRel::getContractId,historyContractId);
        List<ContractHistoryRel> list = contractHistoryRelService.list(wrapper);
        ContractHistoryRel rel = ContractHistoryRel.builder().contractId(contract.getId())
                .historyContractId(historyContractId).build();
        relList.add(rel);
        if(CollectionUtil.isNotEmpty(list)){
            for (ContractHistoryRel contractHistoryRel : list) {
                ContractHistoryRel historyRel = ContractHistoryRel.builder().contractId(contract.getId())
                        .historyContractId(contractHistoryRel.getHistoryContractId()).build();
                relList.add(historyRel);
            }
        }

        contractHistoryRelService.saveBatch(relList);
        Contract build = Contract.builder().build();
        build.setId(contract.getId());
        return build;
    }

    private void invalidHistoryContract(Long historyContractId) {
        LoginInfo loginInfo = getLoginInfo();

        Contract historyContract = Contract.builder().contractStatusCode(ContractStatus.INVALID.getCode())
                .operatorById(loginInfo == null ? null : loginInfo.getId())
                .operatorByName(loginInfo == null ? null : loginInfo.getName())
                .build();
        historyContract.setId(historyContractId);
        updateById(historyContract);
    }

    private LoginInfo getLoginInfo(){
        LoginInfo loginInfo = null;
        //获取当前登录信息
        Optional<LoginInfo> optional = SecurityUtils.getLoginInfo();
        if(optional.isPresent()){
            loginInfo = optional.get();
        }else{
            log.warn("获取用户登录信息失败");
        }
        return  loginInfo;
    }

    /**
     * 提醒
     * @param contractId 合同ID
     * @return
     */
    @Override
    public boolean remind(String contractId) {
        //发送消息通知
        if(SecurityUtils.getLoginInfo().isPresent()){
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            Contract contract = getById(contractId);
            ContractEvent contractEvent = BeanUtil.copy(contract, ContractEvent.class);
            contractEvent.setContractId(contract.getId());
            contractEvent.setBusinessId(contract.getId());
            defaultEventPublisher.publishEvent(new ContractSendEvent(this,loginInfo,contractEvent,contractEvent.getSupplierCode(),contractEvent.getSupplierName()));
        }
        return true;
    }

    /**
     * 获取合同详情
     *
     * @param id
     * @return
     */
    @Override
    public ContractVo getContractInfo(Long id) {
        Contract contract = getById(id);
        if(contract == null){
            return null;
        }
       /* ContractType contractType = contractTypeService.getById(contract.getContractTypeId());
        if(contractType != null){
            contract.setContractTypeName(contractType.getContractTypeName());
        }*/
        ContractVo contractVo = BeanUtil.copy(contract,ContractVo.class);

        //附件
        buildAttachment(contractVo);

        //历史合同
        /*QueryWrapper<ContractHistoryRel> relQueryWrapper = Condition.getQueryWrapper(ContractHistoryRel.builder().contractId(id).build());
        List<ContractHistoryRel> list = contractHistoryRelService.list(relQueryWrapper);
        if(CollectionUtil.isNotEmpty(list)){
            List<Long> historyList = list.stream().map(ContractHistoryRel::getHistoryContractId).collect(Collectors.toList());
            Contract contractQuery = Contract.builder().build();
            QueryWrapper<Contract> contractQueryWrapper = Condition.getQueryWrapper(contractQuery);
            contractQueryWrapper.lambda().in(Contract::getId,historyList);
            List<Contract> historyContracts = list(contractQueryWrapper);
            contractVo.setHistoryContracts(historyContracts);
        }*/

        //设置可操作按钮
        buildPermissionButton(contractVo);

        //字典转换
        if (StringUtil.equals(contractVo.getContractStatusCode(), ContractStatus.DRAFT.getCode())) {
            contractVo.setContractStatusCode(ContractStatus.DRAFT.getName());
        } else if (StringUtil.equals(contractVo.getContractStatusCode(), ContractStatus.TO_BE_ANSWERED.getCode())) {
            contractVo.setContractStatusCode(ContractStatus.TO_BE_ANSWERED.getAlias());
        } else if (StringUtil.equals(contractVo.getContractStatusCode(), ContractStatus.VALID.getCode())) {
            contractVo.setContractStatusCode(ContractStatus.VALID.getName());
        } else if (StringUtil.equals(contractVo.getContractStatusCode(), ContractStatus.WITHDRAWN.getCode())) {
            contractVo.setContractStatusCode(ContractStatus.WITHDRAWN.getName());
        }

        return contractVo;
    }

    /**
     * 作废合同
     *
     * @param contractId
     * @return
     */
    @Override
    @OperationLog(code = "contract_invalid",content = "作废合同")
    public boolean invalid(Long contractId) {
        //修改合同状态
        update(Wrappers.<Contract>update().lambda()
                .set(Contract::getContractStatusCode,ContractStatus.INVALID.getCode())
                .eq(Contract::getId,contractId));

        //发送消息通知
        if(SecurityUtils.getLoginInfo().isPresent()){
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            Contract contract = getById(contractId);
            ContractEvent contractEvent = BeanUtil.copy(contract, ContractEvent.class);
            contractEvent.setContractId(contract.getId());
            contractEvent.setBusinessId(contract.getId());
            defaultEventPublisher.publishEvent(new ContractInvalidEvent(this,loginInfo,contractEvent,contractEvent.getSupplierCode(),contractEvent.getSupplierName()));
        }else{
            log.warn("获取用户登录信息失败");
        }

        return true;
    }

    /**
     * 批量设置可操作按钮
     *
     * @param records
     */
    @Override
    public void buildPermissionButtonBatch(List<ContractVo> records) {
        if(CollectionUtil.isNotEmpty(records)){
            for (ContractVo contractVo : records) {
                buildPermissionButton(contractVo);
            }
        }
    }

    /**
     * 确认合同
     *
     * @param contractId
     * @return
     */
    @Override
//    @OperationLog(code = "contract_confirm",content = "确认合同")
    public boolean confirm(Long contractId) {
        //修改合同状态
        update(Wrappers.<Contract>update().lambda()
                .set(Contract::getContractStatusCode,ContractStatus.VALID.getCode())
                .set(Contract::getContractStatusName,ContractStatus.VALID.getName())
                .set(Contract::getSignedTime,new Date())//确认的时候填写确认时间
                .eq(Contract::getId,contractId));

        //发送消息通知
        if(SecurityUtils.getLoginInfo().isPresent()){
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();

            //记录操作日志
            ContractOperationLog operationLog = ContractOperationLog.builder()
                    .contractId(contractId)
                    .operateContent(ContractOperateType.CONFIRM.getName())
                    .operatorNo(loginInfo.getId())
                    .operatorName(loginInfo.getName()).build();
            operationLogService.save(operationLog);


            Contract contract = getById(contractId);
            ContractEvent contractEvent = BeanUtil.copy(contract, ContractEvent.class);
            contractEvent.setContractId(contract.getId());
            contractEvent.setBusinessId(contract.getId());
            defaultEventPublisher.publishEvent(new ContractConfirmEvent(this,loginInfo,contractEvent,contractEvent.getSupplierCode(),contractEvent.getSupplierName()));
            //结束发送代办事件
           /* HandlerMsgParam handlerMsgParam = new HandlerMsgParam();
            handlerMsgParam.setEventCode(EventCode.contract_send.getCode());
            handlerMsgParam.setBusinessId(contractId);
            handlerMsgParam.setReceiverType(loginInfo.getCurrentSubjectType());
            informationServiceFeignClient.handlerInformation(handlerMsgParam);*/


        }else{
            log.warn("获取用户登录信息失败");
        }





        //fixme 目前无网签接口，跳过网签步骤
        skipSignOnLine(contractId);
        return true;
    }

    /**
     * 网签模拟接口
     * @param contractId 合同ID
     */
    @Deprecated
    private void skipSignOnLine(Long contractId) {
        //修改合同状态,记录网签时间
        update(Wrappers.<Contract>update().lambda()
                .set(Contract::getContractStatusCode,ContractStatus.VALID.getCode())
                .eq(Contract::getId,contractId));

    }

    /**
     * 拒绝合同
     *
     * @param contractId
     * @return
     */
    @Override
    @OperationLog(code = "contract_reject",content = "拒绝合同")
    public boolean reject(Long contractId) {
        //修改合同状态
        update(Wrappers.<Contract>update().lambda()
                .set(Contract::getContractStatusCode,ContractStatus.REJECTED.getCode())
                .eq(Contract::getId,contractId));

        //发送消息通知
        if(SecurityUtils.getLoginInfo().isPresent()){
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            Contract contract = getById(contractId);
            ContractEvent contractEvent = BeanUtil.copy(contract, ContractEvent.class);
            contractEvent.setContractId(contract.getId());
            contractEvent.setBusinessId(contract.getId());
            defaultEventPublisher.publishEvent(new ContractRejectEvent(this,loginInfo,contractEvent,contractEvent.getSupplierCode(),contractEvent.getSupplierName()));


            //结束发送代办事件
            HandlerMsgParam handlerMsgParam = new HandlerMsgParam();
            handlerMsgParam.setEventCode(EventCode.contract_send.getCode());
            handlerMsgParam.setBusinessId(contractId);
            handlerMsgParam.setReceiverType(loginInfo.getCurrentSubjectType());
            informationServiceFeignClient.handlerInformation(handlerMsgParam);



        }else{
            log.warn("获取用户登录信息失败");
        }




        return true;


    }

    /**
     * 设置合同附件
     *
     * @param record
     */
    @Override
    public void buildAttachment(ContractVo record) {
        AttachmentRel attachmentRel = AttachmentRel.builder().businessFormId(record.getId()).build();
        QueryWrapper<AttachmentRel> queryWrapper = Condition.getQueryWrapper(attachmentRel);
        List<AttachmentRel> attachmentRelList = attachmentRelService.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(attachmentRelList)){
            List<FileInfo> attachmentList = new ArrayList<>();
            for (AttachmentRel rel : attachmentRelList) {
                FileInfo fileInfo = FileInfo.builder()
                        .id(Long.valueOf(rel.getAttachmentId()))
                        .originalFilename(rel.getAttachmentName())
                        .url(rel.getAttachmentUrl())
                        .build();
                attachmentList.add(fileInfo);
            }
            record.setAttachmentList(attachmentList);
        }else{
            record.setAttachmentList(new ArrayList<>());
        }
    }

    /**
     * 批量设置合同附件
     *
     * @param records
     */
    @Override
    public void buildAttachmentBatch(List<ContractVo> records) {
        if(CollectionUtil.isNotEmpty(records)){
            for (ContractVo record : records) {
                buildAttachment(record);
            }
        }
    }

    @Autowired
    private ContractMapper contractMapper;

    @Override
    public IPage<Contract> selectPage(IPage<Contract> page, QueryParam<ContractQueryParam> queryParam) {
        List<Contract> contracts = contractMapper.selectListPage(page, queryParam);
        return page.setRecords(contracts);
    }

    /**
     * 设置可操作按钮
     * @param contractVo
     */
    private void buildPermissionButton(ContractVo contractVo) {
        if(contractVo == null){
            return;
        }
        ContractStatus contractStatus = ContractStatus.getTypeByCode(contractVo.getContractStatusCode());
        if(contractStatus == null){
            return;
        }
        switch (contractStatus) {
            case TO_BE_ANSWERED:
                contractVo.setPermissionButton(ContractStatusOperateRel.operations(ContractStatusOperateRel.TO_BE_ANSWERED));
                break;
            default:
        }
    }


}
