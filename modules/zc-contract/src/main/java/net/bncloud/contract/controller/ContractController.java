package net.bncloud.contract.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.IResultCode;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.contract.entity.Contract;
import net.bncloud.contract.entity.ContractType;
import net.bncloud.contract.enums.ContractStatus;
import net.bncloud.contract.enums.TabCategory;
import net.bncloud.contract.param.ContractExistValidateParam;
import net.bncloud.contract.param.ContractQueryParam;
import net.bncloud.contract.param.ContractSaveParam;
import net.bncloud.contract.service.ContractService;
import net.bncloud.contract.service.ContractTypeService;
import net.bncloud.contract.vo.ContractStatisticsVo;
import net.bncloud.contract.vo.ContractVo;
import net.bncloud.contract.wrapper.ContractWrapper;
import net.bncloud.support.Condition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 *
 * zc合同采购接口
 *
 *
 * @author huangtao
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/zc/contract")
@Api(tags = "供应商合同数据接口")
@Slf4j
public class ContractController {


    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractTypeService contractTypeService;


    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入Contract")
    public R<ContractVo> getById(@PathVariable(value = "id") Long id) {
        ContractVo contractVo = contractService.getContractInfo(id);
        contractVo.setEventTypeName("物料采购框架合同(存货相关)");
        return R.data(contractVo);
    }

    /**
     * 新增
     */
    /*@PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入Contract")
    public R<ContractSaveParam> save(@RequestBody @Validated ContractSaveParam contract) {
        R<ContractSaveParam> contractSaveParamR = contractService.saveContract(contract);
        return contractService.saveContract(contract,false);
    }*/

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入id")
    public R delete(@PathVariable(value = "id") String id) {
        contractService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入contract")
    public R<ContractSaveParam> updateById(@RequestBody @Validated ContractSaveParam contract) {
        return contractService.updateContract(contract);
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入contract")
    public R list(@RequestBody Contract contract) {
        List<Contract> list = contractService.list(Condition.getQueryWrapper(contract));

        return R.data(list);
    }


    //*****************分割线*************************

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入ContractParam")
    public R<PageImpl<ContractVo>> page(Pageable pageable, @RequestBody QueryParam<ContractQueryParam> queryParam) {
        //采购商过滤
        Optional<LoginInfo> loginInfoForOptional = SecurityUtils.getLoginInfo();
        LoginInfo loginInfo = loginInfoForOptional.get();
        Org currentOrg = loginInfo.getCurrentOrg();
        if (currentOrg == null || currentOrg.getId() == null) {
            throw new RuntimeException("采购商编码有问题");
        }
        ContractQueryParam contractQueryParam = queryParam.getParam();
        contractQueryParam.setOrgId(currentOrg.getId());
        /*final ContractQueryParam param = queryParam.getParam();
        Contract contract = BeanUtil.copy(param, Contract.class);

        LambdaQueryWrapper<Contract> queryWrapper = Condition.getQueryWrapper(contract).lambda();
        String tabCategory = param.getTabCategory();
        //临近到期
        if (TabCategory.NEAR_EXPIRY.getCode().equals(tabCategory)) {
            Date date = null;
            try {
                date = DateUtil.parse(DateUtil.formatDate(new Date()), DateUtil.PATTERN_DATE);
            } catch (ParseException e) {
                log.error("日期转化异常", e);
            }
            tabCategory = ContractStatus.VALID.getCode();
            queryWrapper.eq(Contract::getContractStatusCode, tabCategory);
            queryWrapper.le(Contract::getExpiryDate, DateUtil.plusDays(date, 30));
        } else if (StringUtils.isNotBlank(tabCategory)) {
            if (!StringUtil.equals(tabCategory, TabCategory.ALL.getCode())) {
                queryWrapper.eq(Contract::getContractStatusCode, tabCategory);
            }
        }

        BigDecimal excludingTaxAmountMin = param.getExcludingTaxAmountMin();
        if (excludingTaxAmountMin != null) {
            queryWrapper.ge(Contract::getExcludingTaxAmount, excludingTaxAmountMin);
        }

        BigDecimal excludingTaxAmountMax = param.getExcludingTaxAmountMax();
        if (excludingTaxAmountMax != null) {
            queryWrapper.le(Contract::getExcludingTaxAmount, excludingTaxAmountMax);
        }

        BigDecimal taxIncludedAmountMin = param.getTaxIncludedAmountMin();
        if (taxIncludedAmountMin != null) {
            queryWrapper.ge(Contract::getTaxIncludedAmount, taxIncludedAmountMin);
        }

        BigDecimal taxIncludedAmountMax = param.getTaxIncludedAmountMax();
        if (taxIncludedAmountMax != null) {
            queryWrapper.le(Contract::getTaxIncludedAmount, taxIncludedAmountMax);
        }

        String signedTimeBegin = param.getSignedTimeBegin();
        if(StringUtil.isNotBlank(signedTimeBegin)){
            queryWrapper.ge(Contract::getSignedTime,signedTimeBegin);
        }

        String signedTimeEnd = param.getSignedTimeEnd();
        if(StringUtils.isNotBlank(signedTimeEnd)){
            queryWrapper.le(Contract::getSignedTime,signedTimeEnd);
        }

        String searchContent = queryParam.getSearchValue();
        if (StringUtil.isNotBlank(searchContent)) {
            queryWrapper.and(
                    qw -> qw.like(Contract::getSupplierCode, searchContent)
                            .or().like(Contract::getSupplierName, searchContent)
                            .or().like(Contract::getContractCode, searchContent)
                            .or().like(Contract::getContractTitle, searchContent)
                            .or().like(Contract::getCustomerCode, searchContent)
                            .or().like(Contract::getCustomerName, searchContent)
            );
        }
        //按签订时间倒序排序
        queryWrapper.orderByDesc(Contract::getSignedTime);

        final IPage<Contract> page = contractService.page(PageUtils.toPage(pageable), queryWrapper);
        IPage<ContractVo> contractVoIPage = ContractWrapper.build().pageVO(page);
        List<ContractType> contractTypeList = contractTypeService.list();
        contractVoIPage.getRecords().forEach(item ->{
            contractTypeList.stream().forEach(type ->{
                if(item.getContractTypeId().toString().equals(type.getId().toString())){
                    item.setContractTypeName(type.getContractTypeName());
                }
            });
        });*/

        //todo
        // 查询接口，条件和字段转换待todo

        //当前的采购商只能查询到自己组织的合同
       /* Optional<LoginInfo> loginInfoForOptional = SecurityUtils.getLoginInfo();
        LoginInfo loginInfo = loginInfoForOptional.get();
        Org currentOrg = loginInfo.getCurrentOrg();
        if (currentOrg == null || currentOrg.getId() == null) {
            log.error("采购商编码错误,{}", JSON.toJSONString(loginInfo));
            throw new RuntimeException("采购商编码有问题");
        }
        //设置查询的orgId
        queryParam.getParam().setOrgId(currentOrg.getId());*/
        //开始查询
        IPage<Contract> contractIPage = contractService.selectPage(PageUtils.toPage(pageable), queryParam);
        IPage<ContractVo> contractVoIPageForPage = ContractWrapper.build().pageVO(contractIPage);
        List<ContractVo> records = contractVoIPageForPage.getRecords();
        for (ContractVo record : records) {
            if(ObjectUtil.isNotEmpty(record.getTaxIncludedAmount())){
                BigDecimal taxIncludedAmount = record.getTaxIncludedAmount();
                record.setTaxRateAmountString(taxIncludedAmount.toString());
            }

        }
        contractService.buildPermissionButtonBatch(records);
        contractService.buildAttachmentBatch(records);

        //字典转换
        for (ContractVo vo : records) {
            vo.setEventTypeName("物料采购框架合同(存货相关)");

            if (StringUtil.equals(vo.getContractStatusCode(), ContractStatus.DRAFT.getCode())) {
                vo.setContractStatusCode(ContractStatus.DRAFT.getName());
            } else if (StringUtil.equals(vo.getContractStatusCode(), ContractStatus.TO_BE_ANSWERED.getCode())) {
                vo.setContractStatusCode(ContractStatus.TO_BE_ANSWERED.getName());
            } else if (StringUtil.equals(vo.getContractStatusCode(), ContractStatus.VALID.getCode())) {
                vo.setContractStatusCode(ContractStatus.VALID.getName());
            } else if (StringUtil.equals(vo.getContractStatusCode(), ContractStatus.WITHDRAWN.getCode())) {
                vo.setContractStatusCode(ContractStatus.WITHDRAWN.getName());
            }
        }

        return R.data(PageUtils.result(contractVoIPageForPage));

//        List<Contract> records = contractIPage.getRecords();
//        List<ContractVo> records = contractVoIPage.getRecords();
//        contractService.buildPermissionButtonBatch(records);
//        contractService.buildAttachmentBatch(records);
//        return R.data(PageUtils.result(contractVoIPage));
    }


    /**
     * 合同数量统计
     */
    @GetMapping("/count")
    @ApiOperation(value = "合同数量统计", notes = "传入contract")
    public R<ContractStatisticsVo> statistics() {
        return R.data(contractService.getStatisticsInfo());
    }


    /**
     * 发送合同
     */
    /*@PostMapping("/send")
    @ApiOperation(value = "发送合同", notes = "传入contract")
    public R send(@RequestBody @Validated ContractSaveParam contract) {
        return R.data(contractService.send(contract));
    }*/
    /**
     * 发送合同
     */
    @PostMapping("/send")
    @ApiOperation(value = "发送合同", notes = "传入contract")
    public R sendV2(Long id) {
        return R.data(contractService.send(id));
    }

    /**
     * 发送合同
     */
    @PostMapping("/{contractId}/sendByContractId")
    @ApiOperation(value = "发送合同", notes = "传入contract")
    public R<Boolean> sendByContractId(@PathVariable(name = "contractId") Long contractId) {
        return R.data(contractService.sendByContractId(contractId,false));
    }

    /**
     * 撤回合同
     */
    @PostMapping("/{contractId}/withdraw")
    @ApiOperation(value = "撤回合同", notes = "传入contract")
    public R<Boolean> withdraw(@PathVariable(name = "contractId") Long contractId) {
        return R.data(contractService.withdraw(contractId));
    }

    /**
     * 重新签约
     */
    /*@PostMapping("/{contractId}/resign")
    @ApiOperation(value = "重新签约", notes = "传入contract")
    public R resign(@PathVariable(name = "contractId") String contractId) {
        return R.data(contractService.resign(contractId));
    }*/

    /**
     * 更新合同，复制生成新合同，原合同（及相关历史合同）成为历史合同数据
     */
    /*@PostMapping("{historyContractId}/copy")
    @ApiOperation(value = "更新合同", notes = "传入contract")
    public R copy(@RequestBody @Validated ContractSaveParam contract,
                  @PathVariable(name = "historyContractId") Long historyContractId) {
        return R.data(contractService.copy(historyContractId, contract));
    }*/

    /**
     * 作废
     */
    /*@PostMapping("{contractId}/invalid")
    @ApiOperation(value = "作废合同", notes = "传入contract")
    public R invalid(@PathVariable(name = "contractId") Long contractId){
        return R.data(contractService.invalid(contractId));
    }*/

    /**
     * 提醒
     */
  /*  @PostMapping("/{contractId}/remind")
    @ApiOperation(value = "提醒", notes = "传入contract")
    public R remind(@PathVariable(name = "contractId") String contractId) {
        return R.data(contractService.remind(contractId));
    }*/

    /**
     * 合同唯一校验
     */
    /*@PostMapping("/exist/validate")
    @ApiOperation(value = "合同参数，唯一校验", notes = "传入contract")
    public R existValidate(@RequestBody ContractExistValidateParam param) {
        contractService.existValidate(param);
        return R.success();
    }*/

}
