package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.BiddingLineExt;
import net.bncloud.quotation.enums.BiddingLineExtBizTypeEnum;
import net.bncloud.quotation.param.BiddingLineExtParam;
import net.bncloud.quotation.vo.BiddingLineExtVo;
import net.bncloud.quotation.vo.QuotationSupplierCompareVo;
import net.bncloud.quotation.vo.QuotationSupplierVo;

import java.util.List;

/**
 * <p>
 * 招标行信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface BiddingLineExtService extends BaseService<BiddingLineExt> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<BiddingLineExt> selectPage(IPage<BiddingLineExt> page, QueryParam<BiddingLineExtParam> pageParam);


    List<BiddingLineExt> queryList(BiddingLineExt biddingLineExt);

    List<QuotationSupplierVo> quotationLineExtlist(Long quotationBaseId, Boolean isAll,List<String> supplierIds);

    QuotationSupplierCompareVo quotationSupplierCompare(Long quotationBaseId);

    @Deprecated
    List<BiddingLineExtVo> selectList(BiddingLineExt biddingLineExt);

    /**
     * 查询上次报价信息
     * @param quotationId 报价单ID
     * @param supplierId 供应商ID
     * @param bizType 期望或正常
     * @return
     * @throws Exception
     */
    List<BiddingLineExt> queryQuotationLastBidding(Long quotationId, Long supplierId, BiddingLineExtBizTypeEnum bizType,String dataType) throws Exception;

    /**
     * 查询当前期望报价
     * @param quotationId 报价单ID
     * @param bizType 期望或正常
     * @return
     * @throws Exception
     */
    List<BiddingLineExt> queryQuotationExceptBidding(Long quotationId,  BiddingLineExtBizTypeEnum bizType,String dataTyp) throws Exception;

    List<BiddingLineExtVo> cheapest(Long quotationBaseId);

    void saveRestateBiddingLineExt(Long quotationBaseId, Integer currentRoundNumber);

    /**
     * 根据报价记录ID ,查询报价信息
     * @param quotationRecordId 报价记录ID
     * @return 报价信息
     */
    List<BiddingLineExt> listByQuotationRecordId(Long quotationRecordId);
}
