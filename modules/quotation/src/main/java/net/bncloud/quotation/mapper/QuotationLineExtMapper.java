package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.QuotationLineExt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.quotation.param.QuotationLineExtParam;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.vo.QuotationLineExtVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 询价行动态行扩展信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface QuotationLineExtMapper extends BaseMapper<QuotationLineExt> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-02-14
     */
    List<QuotationLineExt> selectListPage(IPage page, QueryParam<QuotationLineExtParam> pageParam);

	/**
	 * 删除询价行扩展信息
	 * @param quotationBaseId 询价行基础信息ID
	 */
	void deleteByQuotationBaseId(@Param("quotationBaseId") Long quotationBaseId);

    List<QuotationLineExtVo> selectQuotationLineExtlist(@Param("quotationLineExt") QuotationLineExt quotationLineExt);
}
