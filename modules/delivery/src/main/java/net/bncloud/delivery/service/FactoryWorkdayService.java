package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.entity.DeliveryNoteExcelImportDetailVo;
import net.bncloud.delivery.entity.FactoryWorkday;
import net.bncloud.delivery.param.*;
import net.bncloud.delivery.vo.DeliveryDetailVo;
import net.bncloud.delivery.vo.FactoryWorkdayVo;
import net.bncloud.delivery.vo.PrintDataVo;
import net.bncloud.delivery.vo.ShippableDeliveryPlanDetailItemVo;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 工厂工作日 service 接口
 *
 * @author liyh
 * @since 2021-05-16
 */
public interface FactoryWorkdayService extends BaseService<FactoryWorkday> {


    IPage<FactoryWorkdayVo> selectListPage(IPage<FactoryWorkday> page, QueryParam<FactoryWorkdayParam> queryParam, String code);

    void batchSetWorkday(FactoryWorkdayBatchParam param, String type);

    void deleteBatchWorkday(BatchDeleteWorkDayParam param);

    void deleteAllWorkDayFromFactoryId(List<Long> ids);
}
