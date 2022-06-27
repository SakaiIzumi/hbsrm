package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationBase;
import net.bncloud.quotation.entity.QuotationLineExt;
import net.bncloud.quotation.mapper.QuotationLineExtMapper;
import net.bncloud.quotation.param.QuotationLineExtParam;
import net.bncloud.quotation.service.QuotationLineExtService;
import net.bncloud.quotation.vo.QuotationLineExtVo;
import net.bncloud.support.Condition;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 询价行动态行扩展信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
public class QuotationLineExtServiceImpl extends BaseServiceImpl<QuotationLineExtMapper, QuotationLineExt> implements QuotationLineExtService {

    @Override
    public IPage<QuotationLineExt> selectPage(IPage<QuotationLineExt> page, QueryParam<QuotationLineExtParam> pageParam) {
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }

	/**
	 * 删除询价行扩展信息
 	 * @param quotationBaseId 询价基础信息ID
	 */
    @Override
    public void deleteByQuotationBaseId(Long quotationBaseId) {
		baseMapper.deleteByQuotationBaseId(quotationBaseId);
    }

    @Override
    public List<QuotationLineExt> queryList(QuotationLineExt quotationLineExt) {
        LambdaQueryWrapper<QuotationLineExt> queryWrapper = Condition.getQueryWrapper(new QuotationLineExt())
                .lambda().eq(quotationLineExt.getQuotationBaseId() != null,QuotationLineExt::getQuotationBaseId, quotationLineExt.getQuotationBaseId())
                .orderByAsc(QuotationLineExt::getOrderValue);
        return super.list(queryWrapper);
    }

    @Override
    public List<QuotationLineExtVo> selectQuotationLineExtlist(QuotationLineExt quotationLineExt) {
        return baseMapper.selectQuotationLineExtlist(quotationLineExt);
    }
}
