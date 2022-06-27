package net.bncloud.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialDeliveryDetail;
import net.bncloud.financial.entity.FinancialStatement;
import net.bncloud.financial.param.FinancialDeliveryDetailBatchSaveParam;
import net.bncloud.financial.param.FinancialDeliveryDetailParam;
import net.bncloud.financial.vo.FinancialDeliveryDetailVo;

import java.util.List;

/**
 * <p>
 * 出货明细表信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialDeliveryDetailService extends BaseService<FinancialDeliveryDetail> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<FinancialDeliveryDetail> selectPage(IPage<FinancialDeliveryDetail> page, QueryParam<FinancialDeliveryDetailParam> pageParam);


    /**
     * 保存出货明细
     *
     * @param statementId        对账单Id
     * @param deliveryDetailList 明细数据
     * @return 返回结果
     */
    boolean saveDetails(Long statementId, List<FinancialDeliveryDetail> deliveryDetailList);


    /**
     * 生成对账单-收发货明细
     *
     * @param financialStatement 对账单
     */
    void generateDeliveryDetail(FinancialStatement financialStatement);

    /**
     * 组装对账单送货明细
     * @param statementId
     * @param billId
     * @return
     */
    FinancialDeliveryDetailVo getBuildDeliveryDetail(Long statementId, Long billId);

    /**
     * 批量保存
     *
     * @param batchSaveParam 参数
     */
    void batchSave(FinancialDeliveryDetailBatchSaveParam batchSaveParam);

    /**
     * 删除对账单，收货明细
     *
     * @param id 明细ID
     */
    void deleteDetail(long id);

    /**
     * 删除
     * @param statementId
     */
    void deleteByStatementId(long statementId);

    /**
     * 根据对账单ID，查询金额汇总信息
     *
     * @param statementId 对账单ID
     * @return 金额汇总信息
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
