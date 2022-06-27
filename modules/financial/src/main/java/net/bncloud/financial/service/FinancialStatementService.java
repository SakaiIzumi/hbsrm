package net.bncloud.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialStatement;
import net.bncloud.financial.param.FinancialStatementParam;
import net.bncloud.financial.param.FinancialStatementSaveParam;
import net.bncloud.financial.vo.FinancialStatementStaticsVo;
import net.bncloud.financial.vo.FinancialStatementVo;
import net.bncloud.financial.vo.StatementPurchaseRejectVo;

import java.util.List;

/**
 * <p>
 * 对账单信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialStatementService extends BaseService<FinancialStatement> {


    /**
     * 修改对账单
     *
     * @param financialStatement
     */
    void  updateStatementById(FinancialStatement financialStatement);

    /**
     * 删除对账费用明细
     * 同时，扣减对账单中的 费用含税金额、对账含税金额、未税金额和税额
     * @param costDetailId 费用明细id
     */
    void deleteCostDetailById(Long costDetailId);

    /**
     * 删除对账送货明细
     * 同时，扣减对账单中的 送货含税金额、对账含税金额、未税金额和税额
     * @param deliveryDetailId 送货明细id
     */
    void deleteDeliveryDetailById(Long deliveryDetailId);
    /**
     * 获取对账单状态的统计数量
     *
     * @return
     */
    FinancialStatementStaticsVo getStatisticsInfo(String sourceType);

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<FinancialStatement> selectPage(IPage<FinancialStatement> page, QueryParam<FinancialStatementParam> pageParam);


    /**
     * 对账单信息保存接口
     *
     * @param accountStatementSaveParam 保存参数
     * @return 对账单信息
     */
    FinancialStatementVo saveStatement(FinancialStatementSaveParam accountStatementSaveParam);

    /**
     * 生成对账单明细
     *
     * @param statementId 对账单ID
     */
    void statementDetailGenerate(Long statementId);

    /**
     * 校验供应商是否已冻结
     * @param supplierCodeList
     */
    void checkSupplierStatus(List<String> supplierCodeList);

    /**
     * 校验采购方是否已冻结
     * @param purchaserCodeList
     */
    void checkPurchaserStatus(List<String> purchaserCodeList);

    /**
     * 批量生成对账明细
     *
     * @param ids
     */
    List<FinancialStatementVo> batchStatementGenerateAuto(List<String> ids, String workBench);

    /**
     * 通过id删除对账单
     */
    void deleteById(Long id);

    /**
     * 根据主键ID获取对账单详情
     *
     * @param id 主键ID
     * @return 对账单信息
     */
    FinancialStatementVo getStatementInfo(Long id);

    /**
     * 采购方发送对账单
     *
     * @param statementId
     * @return
     */
    boolean purchaseSend(Long statementId);

    /**
     * 采购方撤回对账单
     *
     * @param statementId 对账单ID
     * @return
     */
    boolean purchaseWithdraw(Long statementId);

    /**
     * 采购方作废对账单
     *
     * @param statementId 对账单ID
     * @return
     */
    boolean purchaseInvalid(Long statementId);

    /**
     * 采购方确认对账单
     *
     * @param statementId 对账单ID
     * @return
     */
    boolean purchaseConfirm(Long statementId);

    /**
     * 采购方提醒
     *
     * @param statementId 对账单ID
     * @return
     */
    boolean purchaseRemind(Long statementId);

    /**
     * 采购方退回
     *
     * @param statementId 对账单ID
     * @param statementPurchaseRejectVo
     * @return
     */
    boolean purchaseReject(Long statementId, StatementPurchaseRejectVo statementPurchaseRejectVo);

    /**
     * 供应商发送
     *
     * @param statementId 对账单ID
     * @return
     */
    boolean supplierSend(Long statementId);

    /**
     * 供应商撤回
     *
     * @param statementId 对账单ID
     * @return
     */
    boolean supplierWithdraw(Long statementId);

    /**
     * 供应商作废
     *
     * @param statementId 对账单ID
     * @return
     */
    boolean supplierInvalid(Long statementId);

    /**
     * 供应商确认
     *
     * @param statementId 对账单ID
     * @return
     */
    boolean supplierConfirm(Long statementId);

    /**
     * 供应商确认
     *
     * @param statementId 对账单ID
     * @return
     */
    boolean supplierRemind(Long statementId);

    /**
     * 供应商退回
     *
     * @param statementId 对账单ID
     * @return
     */
    boolean supplierReject(Long statementId,StatementPurchaseRejectVo statementPurchaseRejectVo);
    /**
     * 设置权限按钮
     *
     * @param records 记录
     */
    void buildPermissionButtonBatch(List<FinancialStatementVo> records);
}
