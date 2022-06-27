package net.bncloud.financial.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.bizutil.number.NumberFactory;
import net.bncloud.common.bizutil.number.NumberType;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.financial.entity.*;
import net.bncloud.financial.enums.FinancialBillTypeEnum;
import net.bncloud.financial.event.statement.CostBillChangeEvent;
import net.bncloud.financial.mapper.FinancialCostBillMapper;
import net.bncloud.financial.param.FinancialCostBillParam;
import net.bncloud.financial.param.FinancialCostBillSaveParam;
import net.bncloud.financial.service.*;
import net.bncloud.financial.vo.FinancialCostBillVo;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 费用单据信息表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Service
@Slf4j
public class FinancialCostBillServiceImpl extends BaseServiceImpl<FinancialCostBillMapper, FinancialCostBill> implements FinancialCostBillService {
    @Autowired
    private DefaultEventPublisher defaultEventPublisher;
    @Autowired
    private FinancialAttachmentService attachmentService;
    @Autowired
    private FinancialCostBillLineService costBillLineService;
    @Autowired
    private FinancialBackReasonService backReasonService;
    @Autowired
    private FinancialOperationLogService operationLogService;
    @Autowired
    private FinancialSettlementPoolService settlementPoolService;

    @Autowired
    private  NumberFactory numberFactory;


    /**
     * 根据id查询费用单详情
     *
     * @param id 费用单id
     * @return 费用单视图
     */
    @Override
    public FinancialCostBillVo getAccountCostBillInfo(Long id, String workBench) {
        //BigDecimal totalAmount = BigDecimal.valueOf(0);
        FinancialCostBill financialCostBill = getById(id);
        if (financialCostBill == null) {
            return null;
        }
        FinancialCostBillVo accountCostBillVo = BeanUtil.copy(financialCostBill, FinancialCostBillVo.class);
        //附件
        buildAttachment(accountCostBillVo);
        //设置可操作按钮
//        buildPermissionButton(accountCostBillVo, workBench);
        //费用明细列表
        LambdaQueryWrapper<FinancialCostBillLine> queryWrapper = new LambdaQueryWrapper<FinancialCostBillLine>().eq(FinancialCostBillLine::getCostBillId, accountCostBillVo.getId().toString());
        List<FinancialCostBillLine> financialCostBillLineList = costBillLineService.list(queryWrapper);
        accountCostBillVo.setFinancialCostBillLineList(financialCostBillLineList);
        return accountCostBillVo;

    }


    /**
     * 设置费用单附件
     *
     * @param record 费用单视图
     */
    @Override
    public void buildAttachment(FinancialCostBillVo record) {
        FinancialAttachment financialAttachment = new FinancialAttachment().setBusinessFormId(record.getId().toString());
        QueryWrapper<FinancialAttachment> queryWrapper = Condition.getQueryWrapper(financialAttachment);
        List<FinancialAttachment> financialAttachmentList = attachmentService.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(financialAttachmentList)) {
            List<FileInfo> attachmentList = new ArrayList<>();
            for (FinancialAttachment rel : financialAttachmentList) {
                FileInfo attachment = FileInfo.builder()
                        .id(Long.valueOf(rel.getAttachmentId()))
                        .originalFilename(rel.getAttachmentName())
                        .url(rel.getAttachmentUrl())
                        .build();
                attachmentList.add(attachment);
            }
            record.setAttachmentList(attachmentList);
        } else {
            record.setAttachmentList(new ArrayList<>());
        }
    }

    @Override
    public IPage<FinancialCostBill> selectPage(IPage<FinancialCostBill> page, QueryParam<FinancialCostBillParam> pageParam) {
        // 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
        //notice.setTenantId(SecureUtil.getTenantId());
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }

    @Override
    public void batchSaveAccountCostBill(List<FinancialCostBillSaveParam> costBillList) {
        for (FinancialCostBillSaveParam param : costBillList) {
            saveAccountCostBill(param);
        }
    }


    /**
     * 保存费用单
     *
     * @param costBill 保存费用单的请求参数
     * @return 返回这个费用单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAccountCostBill(FinancialCostBillSaveParam costBill) {
        log.info("保存费用单接收参数：{}", JSON.toJSONString(costBill));
        if(updateCostBill(costBill)){
            log.info("该单号存在，已更新：{}", costBill.getErpBillNo());
            return;
        }
        //费用单信息
        costBill.setId(null);
        if (SecurityUtils.getLoginInfo().isPresent()) {
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            costBill.setCreatedBy(loginInfo.getId());
        }
        //costBill.setCostBillNo(new NumberFactory(NumberType.cost).buildNumber());
        costBill.setCostBillNo(numberFactory.buildNumber(NumberType.cost));
        save(costBill);

        /*//附件信息
        List<FileInfo> attachmentList = costBill.getAttachmentList();
        List<FinancialAttachment> financialAttachments = attachmentService.buildAttachmentRelList(costBill.getId(), attachmentList);
        attachmentService.batchSave(financialAttachments);*/

        //费用明细
        List<FinancialCostBillLine> costBillLineList = costBill.getCostBillLineList();
        costBillLineList.forEach(s -> {
            s.setCostBillId(costBill.getId().toString());
            costBillLineService.save(s);
        });

        FinancialSettlementPool financialSettlementPool = buildFinancialSettlementPool(costBill);
        settlementPoolService.save(financialSettlementPool);
    }

    private FinancialSettlementPool buildFinancialSettlementPool(FinancialCostBillSaveParam costBill) {
        FinancialSettlementPool settlementPool = new FinancialSettlementPool();
        settlementPool.setErpBillId(costBill.getErpBillId());
        settlementPool.setBillId(costBill.getId());
        settlementPool.setErpBillType(costBill.getErpBillType());
        settlementPool.setErpBillNo(costBill.getErpBillNo());
        settlementPool.setBillNo(costBill.getCostBillNo());
        settlementPool.setBillType(FinancialBillTypeEnum.COST.name());
        settlementPool.setCustomerCode(costBill.getCustomerCode());
        settlementPool.setCustomerName(costBill.getCustomerName());
        settlementPool.setSupplierCode(costBill.getSupplierCode());
        settlementPool.setSupplierName(costBill.getSupplierName());
        settlementPool.setCurrencyCode(costBill.getCurrencyCode());
        settlementPool.setCurrencyName(costBill.getCurrencyName());
        settlementPool.setStatementCreated("N");
        settlementPool.setAmount(costBill.getAllAmount());
        FinancialCostBillLine costBillLine = costBill.getCostBillLineList().get(0);
        BigDecimal fEntryTaxRate = costBillLine.getTaxRate();
        settlementPool.setTaxRate(fEntryTaxRate);
        boolean includeTax = Objects.nonNull(fEntryTaxRate) && fEntryTaxRate.compareTo(BigDecimal.ZERO) > 0;
        settlementPool.setHaveTax(includeTax);
        settlementPool.setConfirmTime(costBill.getConfirmTime());
        return settlementPool;
    }

    private boolean updateCostBill(FinancialCostBillSaveParam costBill){
        FinancialCostBill bill = getOne(Wrappers.<FinancialCostBill>query().lambda().eq(FinancialCostBill::getErpBillId, costBill.getErpBillId()));
        if(Objects.isNull(bill)){
            return false;
        }
        if (SecurityUtils.getLoginInfo().isPresent()) {
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            costBill.setCreatedBy(loginInfo.getId());
        }
        costBill.setId(bill.getId());
        costBill.setCustomerCode(bill.getCustomerCode());
        costBill.setCustomerName(bill.getCustomerName());
        costBill.setSupplierCode(bill.getSupplierCode());
        costBill.setSupplierName(bill.getSupplierName());
        costBill.setCostBillNo(bill.getCostBillNo());
        updateById(costBill);

        //费用明细
        List<FinancialCostBillLine> costBillLineList = costBill.getCostBillLineList();
        costBillLineList.forEach(s -> {
            FinancialCostBillLine one = costBillLineService.getOne(Wrappers.<FinancialCostBillLine>query()
                    .lambda().eq(FinancialCostBillLine::getErpLineId, s.getErpLineId()));
            s.setCostBillId(bill.getId().toString());
            if(Objects.nonNull(one)){
                s.setId(one.getId());
                costBillLineService.updateById(s);
            }else {
                s.setId(null);
                costBillLineService.save(s);
            }
        });
        applicationEventPublisher.publishEvent(new CostBillChangeEvent(this,bill.getId()));
        return true;
    }


}
