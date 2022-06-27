package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.FactoryWorkday;
import net.bncloud.delivery.param.BatchDeleteWorkDayParam;
import net.bncloud.delivery.param.FactoryWorkdayBatchParam;
import net.bncloud.delivery.param.FactoryWorkdayParam;
import net.bncloud.delivery.param.OverAllAdjustParam;
import net.bncloud.delivery.vo.FactoryWorkdayVo;

import java.util.List;

/**
 * 工厂工作日 service 接口
 *
 * @author liyh
 * @since 2021-05-16
 */
public interface NewFactoryWorkdayService extends BaseService<FactoryWorkday> {


    IPage<FactoryWorkdayVo> selectListPage(IPage<FactoryWorkday> page, QueryParam<FactoryWorkdayParam> queryParam, String code);

    void batchSetWorkday(FactoryWorkdayBatchParam param, String type);

    void deleteBatchWorkday(BatchDeleteWorkDayParam param);

    void deleteAllWorkDayFromFactoryId(List<Long> ids);

    /**
     * 销售-工作日删除(支持批量)
     */
    void deleteBatchWorkdayForSupplier(BatchDeleteWorkDayParam param);

    /**
     * 调整节日的方法
     * @param factoryWorkdayList 每一次循环的工作日list
     *
     * */
    void holidayAdjustment(List<FactoryWorkday> factoryWorkdayList);

    IPage<FactoryWorkdayVo> supplierPage(IPage<FactoryWorkday> page, QueryParam<FactoryWorkdayParam> queryParam);
}
