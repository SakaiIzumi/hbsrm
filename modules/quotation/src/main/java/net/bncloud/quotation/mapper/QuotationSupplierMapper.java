package net.bncloud.quotation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationSupplier;
import net.bncloud.quotation.param.QuotationSupplierParam;
import net.bncloud.quotation.vo.QuotationSupplierVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 询价供应商信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-18
 */
public interface QuotationSupplierMapper extends BaseMapper<QuotationSupplier> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-02-18
     */
    List<QuotationSupplierVo> selectListPage(IPage page, QueryParam<QuotationSupplierParam> pageParam);

	/**
	 * 删除供应商信息
	 * @param quotationBaseId
	 */
	void deleteByQuotationBaseId(@Param("quotationBaseId") Long quotationBaseId);

}
