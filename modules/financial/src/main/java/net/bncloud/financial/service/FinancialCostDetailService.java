package net.bncloud.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialCostDetail;
import net.bncloud.financial.entity.FinancialStatement;
import net.bncloud.financial.param.FinancialCostDetailBatchSaveParam;
import net.bncloud.financial.param.FinancialCostDetailParam;
import net.bncloud.financial.vo.FinancialCostDetailVo;

import java.util.List;

/**
 * <p>
 * 费用信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialCostDetailService extends BaseService<FinancialCostDetail> {

    /**
     * 保存明细信息
     *
     * @param statementId                  对账单ID
     * @param receivableDiscountDetailList 费用明细
     * @return 返回结果
     */
    boolean saveDetails(Long statementId, List<FinancialCostDetail> receivableDiscountDetailList);

    /**
     * 分页查询
     *
     * @param toPage
     * @param pageParam 分页参数
     * @return
     */
    IPage<FinancialCostDetail> selectPage(IPage<FinancialCostDetail> toPage, QueryParam<FinancialCostDetailParam> pageParam);

    /**
     * 生成对账单-费用明细
     *
     * @param financialStatement 对账单信息
     */
    void generateCostDetail(FinancialStatement financialStatement);

    /**
     * 组装对账单费用明细
     * @param statementId
     * @param billId
     * @return
     */
    FinancialCostDetailVo getBuildCostDetail(Long statementId, Long billId);

    /**
     * 批量保存
     *
     * @param batchSaveParam 折让明细保存参数
     */
    void batchSave(FinancialCostDetailBatchSaveParam batchSaveParam);

    /**
     * 删除
     *
     * @param id 主键ID
     */
    void deleteById(long id);

    /**
     * 删除
     * @param statementId
     */
    void deleteByStatementId(long statementId);

    /**
     * 查询金额汇总信息
     *
     * @param statementId 对账单ID
     * @return 对账单金额信息
     */
    FinancialStatement querySummaryAmountByStatementId(Long statementId);

    /**
     * 查询下一个项次
     *
     * @param statementId 对账单ID
     * @return 下一个项次编号
     */
    String queryNextItemNo(Long statementId);
}
