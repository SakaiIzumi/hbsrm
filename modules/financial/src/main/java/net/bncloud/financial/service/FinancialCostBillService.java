package net.bncloud.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialCostBill;
import net.bncloud.financial.param.FinancialCostBillParam;
import net.bncloud.financial.param.FinancialCostBillSaveParam;
import net.bncloud.financial.vo.FinancialCostBillVo;

import java.util.List;

/**
 * <p>
 * 费用单据信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialCostBillService extends BaseService<FinancialCostBill> {

    /**
     * 自定义分页
     *
     * @param pageParam
     * @return
     */
    IPage<FinancialCostBill> selectPage(IPage<FinancialCostBill> page, QueryParam<FinancialCostBillParam> pageParam);

    /**
     * 费用单详情
     *
     * @param id
     * @return
     */
    FinancialCostBillVo getAccountCostBillInfo(Long id, String workBench);


    /**
     * 设置费用单附件
     *
     * @param record
     */
    void buildAttachment(FinancialCostBillVo record);

    /**
     * 批量新增费用单
     *
     * @return
     */
    void batchSaveAccountCostBill(List<FinancialCostBillSaveParam> costBillList);

    /**
     * 新增费用单
     *
     * @return
     */
    void saveAccountCostBill(FinancialCostBillSaveParam param);

}
