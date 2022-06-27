package net.bncloud.delivery.service;

import net.bncloud.base.BaseService;
import net.bncloud.delivery.entity.DataConfig;

/**
 * @author ddh
 * @description
 * @since 2022/5/19
 */
public interface DataConfigService extends BaseService<DataConfig> {
    /**
     * 更新mrp计划订单最新运算编号
     * @param computerNo
     */
    void updateCurrentMrpPlanOrderComputerNo(String computerNo);

    /**
     * 更新mrp计划订单上版运算编号
     * @param computerNo
     */
    void updatePreMrpPlanOrderComputerNo(String computerNo);

    /**
     * 获取mrp计划订单最新运算编号
     * @return
     */
    String getMrpPlanOrderComputerNo();

}
