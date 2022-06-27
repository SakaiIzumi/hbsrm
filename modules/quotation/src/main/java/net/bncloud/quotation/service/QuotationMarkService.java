package net.bncloud.quotation.service;

import net.bncloud.quotation.entity.QuotationMark;
import net.bncloud.quotation.vo.QuotationMarkVo;
import net.bncloud.quotation.param.QuotationMarkParam;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;

import java.util.List;

/**
 * <p>
 * 询价单应标关联表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-01
 */
public interface QuotationMarkService extends BaseService<QuotationMark> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<QuotationMark> selectPage(IPage<QuotationMark> page, QueryParam<QuotationMarkParam> pageParam);

    /**
     * 根据询价单ID获取当前供应商的的mark信息 权限是当前供应商
     *
     * @param quotationId 询价单ID
     * @return 报价轮次
     * @throws Exception
     */
    QuotationMark selectByQuotationIdAndRoundNum(Long quotationId, Integer roundNum) throws Exception;

    void reject(QuotationMark quotationMark);

    boolean check(QuotationMark quotationMark);

    List<QuotationMarkVo> markedSupplier(QuotationMark quotationMark);
}
