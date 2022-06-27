package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.*;
import net.bncloud.quotation.param.PricingRecordParam;
import net.bncloud.quotation.vo.QuotationSupplierVo;
import net.bncloud.quotation.vo.TRfqQuotationSupplierVo;
import net.bncloud.quotation.vo.TRfqVerificationCodeVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 定价请示记录信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface PricingRecordService extends BaseService<PricingRecord> {

		/**
         * 自定义分页
         * @param page
         * @param pageParam
         * @return
         */
		IPage<PricingRecord> selectPage(IPage<PricingRecord> page, QueryParam<PricingRecordParam> pageParam);


    void savePricingInfo(PricingRecordAndRemark pricingRecordAndRemark);

    List<QuotationAttachment> saveFile(MultipartFile[] files) throws Exception;

    Boolean verifyOpenPricingInfo(TRfqVerificationCodeVo tRfqVerificationCodeVo);

    Boolean sendVerifyCode(TRfqVerificationCodeVo tRfqVerificationCodeVo);

    void sendRestateMsg(List<QuotationSupplier> quotationSuppliers, Long quotationBaseId);

    Boolean sendVerifyCode2Supplier(TRfqVerificationCodeVo tRfqVerificationCodeVo);

    Boolean bidResponse(TRfqQuotationSupplierVo quotationSupplierVo);

    List<QuotationSupplierVo> getPricingInfo(Long id);

}
