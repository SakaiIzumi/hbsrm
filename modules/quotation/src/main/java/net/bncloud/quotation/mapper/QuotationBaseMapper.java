package net.bncloud.quotation.mapper;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.quotation.param.QuotationBaseParam;
import net.bncloud.quotation.vo.QuotationStaticCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 询价基础信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface QuotationBaseMapper extends BaseMapper<QuotationBase> {
	/**
	 * <p>
	 * 自定义分页
	 * </p>
	 *
	 * @author Auto-generator
	 * @since 2022-02-14
	 */
	List<QuotationBase> selectListPage(IPage page, QueryParam<QuotationBaseParam> pageParam);

	List<QuotationBase> selectQuotationBaseSalePage(IPage<QuotationBase> page, @Param("pageParam") QueryParam<QuotationBaseParam> pageParam);

    Integer queryQuotationStatusCount(@Param("supplierId") Long supplierId, @Param("quotationStatus") String quotationStatus);

	/**
	 * 供应商应标数量 +1
	 *
	 * @param quotationBaseId 询价单基础信息ID
	 * @return 修改记录数
	 */
	Integer responseNumIncrease(@Param("quotationBaseId") Long quotationBaseId);

	/**
	 * 当前轮次 +1 ,状态改为 fresh
	 * @param quotationBaseId 询价单ID
	 * @return 修改记录数
	 */
    Integer updateCurrentRoundNumber(@Param("quotationBaseId") Long quotationBaseId);

    int draftForObsoleteCount(Long SupplierId);

}
