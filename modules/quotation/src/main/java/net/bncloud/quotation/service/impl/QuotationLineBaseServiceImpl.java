package net.bncloud.quotation.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.quotation.entity.QuotationLineBase;
import net.bncloud.quotation.entity.QuotationLineExt;
import net.bncloud.quotation.mapper.QuotationLineBaseMapper;
import net.bncloud.quotation.param.QuotationLineBaseParam;
import net.bncloud.quotation.service.QuotationLineBaseService;
import net.bncloud.quotation.service.QuotationLineExtService;
import net.bncloud.quotation.vo.QuotationLineBaseVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 询价行基础信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
public class QuotationLineBaseServiceImpl extends BaseServiceImpl<QuotationLineBaseMapper, QuotationLineBase> implements QuotationLineBaseService {

    private final QuotationLineExtService quotationLineExtService;

    public QuotationLineBaseServiceImpl(QuotationLineExtService quotationLineExtService) {
        this.quotationLineExtService = quotationLineExtService;
    }

    @Override
    public IPage<QuotationLineBase> selectPage(IPage<QuotationLineBase> page, QueryParam<QuotationLineBaseParam> pageParam) {
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }

    /**
     * 保存询价行信息
     *
     * @param quotationLineBase 询价行信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveInfo(QuotationLineBaseVo quotationLineBase) {
        //TODO 校验quotation_base_id，是否已存在
        save(quotationLineBase);
        cascadeSaveQuotationLineExt(quotationLineBase);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateInfo(QuotationLineBaseVo quotationLineBase) {
        updateById(quotationLineBase);
        clearQuotationLineExt(quotationLineBase.getQuotationBaseId());
        cascadeSaveQuotationLineExt(quotationLineBase);
    }

    /**
     * 根据询价单基础信息ID查询询价行信息
     * @param quotationBaseId 询价单基础信息ID
     * @return 询价行信息
     */
    @Override
    public QuotationLineBaseVo getByQuotationBaseId(Long quotationBaseId) {
        QuotationLineBase quotationLineBase = baseMapper.getByQuotationBaseId(quotationBaseId);
        if(quotationLineBase == null){
            return null;
        }
        QuotationLineExt quotationLineExt = new QuotationLineExt();
        quotationLineExt.setQuotationBaseId(quotationBaseId);
        List<QuotationLineExt> quotationLineExtList = quotationLineExtService.queryList(quotationLineExt);
        //排序
        quotationLineExtList=quotationLineExtList.stream().sorted(Comparator.comparing(QuotationLineExt::getOrderValue)).collect(Collectors.toList());
        QuotationLineBaseVo quotationLineBaseVo = BeanUtil.copyProperties(quotationLineBase, QuotationLineBaseVo.class);
        assert quotationLineBaseVo != null;
        quotationLineBaseVo.setQuotationLineExtList(quotationLineExtList);

        return quotationLineBaseVo;
    }

    private void clearQuotationLineExt(Long quotationBaseId) {
        quotationLineExtService.deleteByQuotationBaseId(quotationBaseId);
    }

    /**
     * 级联保存扩展信息
     * @param quotationLineBase 询价行信息
     */
    private void cascadeSaveQuotationLineExt(QuotationLineBaseVo quotationLineBase) {
        List<QuotationLineExt> quotationLineExtList = quotationLineBase.getQuotationLineExtList();
        if (CollectionUtil.isNotEmpty(quotationLineExtList)) {
            AtomicInteger orderValue = new AtomicInteger();
            List<QuotationLineExt> lineExtList = quotationLineExtList.stream().map(
                    item -> {
                        QuotationLineExt quotationLineExt = new QuotationLineExt();
                        BeanUtil.copyProperties(item, quotationLineExt, "id");
                        quotationLineExt.setLineId(quotationLineBase.getId());
                        quotationLineExt.setQuotationBaseId(quotationLineBase.getQuotationBaseId());
                        quotationLineExt.setOrderValue(orderValue.incrementAndGet());
                        return quotationLineExt;
                    }
            ).collect(Collectors.toList());

            quotationLineExtService.saveBatch(lineExtList);

            quotationLineBase.setExtContent(JSON.toJSONString(lineExtList));
            updateById(quotationLineBase);
        }


    }
}
