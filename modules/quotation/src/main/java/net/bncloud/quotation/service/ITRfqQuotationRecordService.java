package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.TRfqQuotationRecord;
import net.bncloud.quotation.param.TRfqQuotationRecordParam;
import net.bncloud.quotation.vo.TRfqQuotationRecordVo;

import java.util.List;

/**
 * <p>
 * 报价记录信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-25
 */
public interface ITRfqQuotationRecordService extends BaseService<TRfqQuotationRecord> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<TRfqQuotationRecordVo> selectPage(IPage<TRfqQuotationRecordVo> page, QueryParam<TRfqQuotationRecordParam> pageParam);


    List<TRfqQuotationRecord> querylistOrderByTimes(TRfqQuotationRecord tRfqQuotationRecord, Boolean isAsc);


    List<TRfqQuotationRecord> querylistLately(Long quotationBaseId);
}
