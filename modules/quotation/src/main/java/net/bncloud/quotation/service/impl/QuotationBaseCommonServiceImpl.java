package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.globallock.lock.LockWrapper;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.quotation.entity.QuotationBase;
import net.bncloud.quotation.entity.QuotationSupplier;
import net.bncloud.quotation.enums.QuotationResultCode;
import net.bncloud.quotation.enums.QuotationScopeEnum;
import net.bncloud.quotation.service.*;
import net.bncloud.quotation.vo.QuotationBaseCommonVO;
import net.bncloud.quotation.vo.QuotationBaseVo;
import net.bncloud.quotation.vo.QuotationLineBaseVo;
import net.bncloud.service.api.platform.supplier.dto.SuppliersDTO;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import net.bncloud.support.Condition;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 询价单通用service实现类
 * @author Toby
 */
@Service
@Slf4j
public class QuotationBaseCommonServiceImpl implements QuotationBaseCommonService {

    private static final String CLEAN_AND_SAVE_LOCK_PREFIX_KEY = "QUOTATION_LOCK:cleanAndSaveSuppliers";

    @Resource
    private SupplierFeignClient supplierFeignClient;

    /**
     * 询价基础信息
     */
    private final QuotationBaseService quotationBaseService;

    /**
     * 询价行基础信息
     */
    private final QuotationLineBaseService quotationLineBaseService;

    /**
     * 询价供应商信息
     */
    private final QuotationSupplierService quotationSupplierService;

    private final DistributedLock distributedLock;

    public QuotationBaseCommonServiceImpl(QuotationBaseService quotationBaseService, QuotationLineBaseService quotationLineBaseService, QuotationSupplierService quotationSupplierService, DistributedLock distributedLock) {
        this.quotationBaseService = quotationBaseService;
        this.quotationLineBaseService = quotationLineBaseService;
        this.quotationSupplierService = quotationSupplierService;
        this.distributedLock = distributedLock;
    }


    /**
     * 询价单通用保存接口
     *
     * @param quotationBaseCommonVo 询价单通用保存VO
     * @return 询价单基础信息 ID
     */
    @Override
    public Long commonSave(QuotationBaseCommonVO quotationBaseCommonVo) {
        QuotationBaseVo quotationBaseVo = quotationBaseCommonVo.getQuotationBaseVo();
        Long id = quotationBaseVo.getId();
        if(id != null ){
            updateCommonInfo(quotationBaseCommonVo);
        }else{
            createCommonInfo(quotationBaseCommonVo);
        }
        return quotationBaseVo.getId();
    }

    /**
     * 清空并保存供应商信息
     * @param quotationBaseId 询价单基础信息ID
     * @param quotationSupplierList 询价单供应商信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cleanAndSaveSuppliers(Long quotationBaseId, List<QuotationSupplier> quotationSupplierList) {
        String lockKey  = CLEAN_AND_SAVE_LOCK_PREFIX_KEY + quotationBaseId;
        LockWrapper lockWrapper = new LockWrapper().setKey(lockKey).setWaitTime(0).setLeaseTime(10).setUnit(TimeUnit.MINUTES);;
        distributedLock.tryLock(lockWrapper,()->{
            QuotationBase quotationBase = quotationBaseService.getById(quotationBaseId);
            if (quotationBase == null) {
                throw new BizException(QuotationResultCode.SOURCE_NOT_FOUND);
            }
            quotationSupplierService.deleteByQuotationBaseId(quotationBaseId);
            String quotationScope=quotationBase.getQuotationScope();
            //指定询价
            if(CollectionUtil.isNotEmpty(quotationSupplierList) && QuotationScopeEnum.SPECIFIED.getCode().equals(quotationScope)){
                List<QuotationSupplier> supplierList = quotationSupplierList.stream().map(item -> {

                    QuotationSupplier quotationSupplier = new QuotationSupplier();
                    BeanUtils.copyProperties(item, quotationSupplier, "id");
                    quotationSupplier.setQuotationBaseId(quotationBaseId);
                    quotationSupplier.setSupplierType("formal");
                    quotationSupplier.setPlatformType("joined");
                    R<SuppliersDTO> result = supplierFeignClient.querySupplierInformation(item.getSupplierId());
                    if(result.isSuccess() && result.getData()!=null){
                        quotationSupplier.setPhone(result.getData().getManagerMobile());
                        quotationSupplier.setReceiver(result.getData().getManagerName());
                        quotationSupplier.setSupplierName(result.getData().getName());
                        quotationSupplier.setSupplierCode(result.getData().getCode());
                    }
                    return quotationSupplier;
                }).collect(Collectors.toList());

                quotationSupplierService.saveBatch(supplierList);
            }

            //公开询价
            if(QuotationScopeEnum.OPEN.getCode().equals(quotationScope)){
                R<List<SuppliersDTO>> result = supplierFeignClient.getSupplierInfoAll();
                if(result.isSuccess()){
                    List<SuppliersDTO> suppliersDTOList = result.getData();
                    List<QuotationSupplier> supplierList = suppliersDTOList.stream().map(item->{
                        QuotationSupplier quotationSupplier = new QuotationSupplier();
                        quotationSupplier.setSupplierId(item.getId());
                        quotationSupplier.setSupplierCode(item.getCode());
                        quotationSupplier.setSupplierName(item.getName());
                        quotationSupplier.setPhone(item.getManagerMobile());
                        quotationSupplier.setReceiver(item.getManagerName());
                        quotationSupplier.setSupplierType("formal");
                        quotationSupplier.setPlatformType("joined");
                        quotationSupplier.setQuotationBaseId(quotationBaseId);
                        return quotationSupplier;
                    }).collect(Collectors.toList());

                    quotationSupplierService.saveBatch(supplierList);
                }else{
                    throw new ApiException(ResultCode.FAILURE.getCode(),"平台服务暂时不可用，获取供应商信息失败!");
                }
            }

            //更新询价单供应商数量
            updateSupplierNum(quotationBaseId);

            return "清空并保存供应商信息成功";
        },()->{
            throw new BizException(QuotationResultCode.REQUEST_REPEAT);
        });


    }

    /**
     * 更新供应商数量
     * @param quotationBaseId 询价单ID
     */
    private void updateSupplierNum(Long quotationBaseId) {
        QuotationSupplier quotationSupplier = new QuotationSupplier();
        quotationSupplier.setQuotationBaseId(quotationBaseId);
        int supplierNum = quotationSupplierService.count(Condition.getQueryWrapper(quotationSupplier));
        //修改询价单供应商数量
        quotationBaseService.update(Wrappers.<QuotationBase>update().lambda()
                .set(QuotationBase::getSupplierNum, supplierNum)
                .eq(QuotationBase::getId,quotationBaseId));
    }





    /**
     * 新增询价单
     * @param quotationBaseCommonVo 询价单信息
     */
    private void createCommonInfo(QuotationBaseCommonVO quotationBaseCommonVo) {
        QuotationBaseVo quotationBaseVo = quotationBaseCommonVo.getQuotationBaseVo();
        if(quotationBaseVo == null){
            log.error("询价单基础信息不能为空");
            throw new BizException(QuotationResultCode.PARAM_ERROR);
        }
        quotationBaseVo.setId(null);
        quotationBaseService.saveInfo(quotationBaseVo);
        Long quotationBaseId = quotationBaseVo.getId();

        QuotationLineBaseVo quotationLineBaseVo = quotationBaseCommonVo.getQuotationLineBaseVo();
        if(quotationLineBaseVo!=null){
            quotationLineBaseVo.setQuotationBaseId(quotationBaseId);
            quotationLineBaseService.saveInfo(quotationLineBaseVo);
        }

        List<QuotationSupplier> quotationSupplierList = quotationBaseCommonVo.getQuotationSupplierList();
        if(CollectionUtil.isNotEmpty(quotationSupplierList)){
            this.cleanAndSaveSuppliers(quotationBaseId,quotationSupplierList);
        }

    }

    /**
     * 更新询价单
     * @param quotationBaseCommonVo 询价单信息
     */
    private void updateCommonInfo(QuotationBaseCommonVO quotationBaseCommonVo) {
        QuotationBaseVo quotationBaseVo = quotationBaseCommonVo.getQuotationBaseVo();
        Long quotationBaseId = quotationBaseVo.getId();
        QuotationBase quotationBase = quotationBaseService.getById(quotationBaseId);
        if(quotationBase == null){
            throw new BizException(QuotationResultCode.SOURCE_NOT_FOUND);
        }

        quotationBaseService.updateInfo(quotationBaseVo);

        QuotationLineBaseVo quotationLineBaseVo = quotationBaseCommonVo.getQuotationLineBaseVo();
        if(quotationLineBaseVo != null){
            //新增OR更新
            Long lineBaseId = quotationLineBaseVo.getId();
            if(lineBaseId == null){
                quotationLineBaseService.saveInfo(quotationLineBaseVo);
            }else{
                quotationLineBaseService.updateInfo(quotationLineBaseVo);
            }
        }

        List<QuotationSupplier> quotationSupplierList = quotationBaseCommonVo.getQuotationSupplierList();
        if(CollectionUtil.isNotEmpty(quotationSupplierList)){
            this.cleanAndSaveSuppliers(quotationBaseId,quotationSupplierList);
        }


    }
}
