package net.bncloud.quotation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.BidResponse;
import net.bncloud.quotation.entity.QuotationSupplier;
import net.bncloud.quotation.param.QuotationSupplierParam;
import net.bncloud.quotation.vo.QuotationSupplierVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 发送应标供应商信息Mapper
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-18
 */
public interface BidResponseMapper extends BaseMapper<BidResponse> {

}
