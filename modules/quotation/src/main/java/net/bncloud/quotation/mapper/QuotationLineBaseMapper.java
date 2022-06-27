package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.QuotationLineBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;


import net.bncloud.quotation.param.QuotationLineBaseParam;
import net.bncloud.common.base.domain.QueryParam;

import java.util.List;

/**
 * <p>
 * 询价行基础信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface QuotationLineBaseMapper extends BaseMapper<QuotationLineBase> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-02-14
     */
    List<QuotationLineBase> selectListPage(IPage page, QueryParam<QuotationLineBaseParam> pageParam);

    /**
     * 查询询价行信息
     * @param quotationBaseId
     * @return
     */
    QuotationLineBase getByQuotationBaseId(Long quotationBaseId);
}
