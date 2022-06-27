package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.BiddingLineExt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.quotation.entity.TRfqQuotationRecord;
import net.bncloud.quotation.enums.BiddingLineExtBizTypeEnum;
import net.bncloud.quotation.param.BiddingLineExtParam;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.vo.BiddingLineExtVo;
import net.bncloud.quotation.vo.QuotationMinAmount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 招标行信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface BiddingLineExtMapper extends BaseMapper<BiddingLineExt> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-02-14
     */
    List<BiddingLineExt> selectListPage(IPage page, QueryParam<BiddingLineExtParam> pageParam);

    List<BiddingLineExtVo> selectBiddingLineList(@Param("biddingLineExt") BiddingLineExt biddingLineExt);

    Integer selectLastRoundNum(@Param("quotationId") Long quotationId, @Param("supplierId") Long supplierId,@Param("bizType") BiddingLineExtBizTypeEnum bizType,@Param("currentRoundNumber") Integer currentRoundNumber);

    List<BiddingLineExtVo> queryCheapest(@Param("tRfqQuotationRecords") List<TRfqQuotationRecord> tRfqQuotationRecords);


    /**
     * 查询最小金额字段信息
     * @param quotationRecordIdList 报价记录ID
     * @return  最小金额字段信息
     */
    List<QuotationMinAmount> queryMinAmount(@Param("quotationRecordIdList") List quotationRecordIdList);
}
