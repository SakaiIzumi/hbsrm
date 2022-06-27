package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Platform;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.enums.SubjectType;
import net.bncloud.quotation.entity.QuotationSupplier;
import net.bncloud.quotation.entity.TRfqQuotationRecord;
import net.bncloud.quotation.mapper.TRfqQuotationRecordMapper;
import net.bncloud.quotation.param.QuotationBaseParam;
import net.bncloud.quotation.service.ITRfqQuotationRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.quotation.vo.TRfqQuotationRecordVo;
import net.bncloud.quotation.param.TRfqQuotationRecordParam;
import net.bncloud.quotation.wrapper.BiddingLineExtWrapper;
import net.bncloud.quotation.wrapper.TRfqQuotationRecordWrapper;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 报价记录信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-25
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TRfqQuotationRecordServiceImpl extends BaseServiceImpl<TRfqQuotationRecordMapper, TRfqQuotationRecord> implements ITRfqQuotationRecordService {

    @Override
    public IPage<TRfqQuotationRecordVo> selectPage(IPage<TRfqQuotationRecordVo> page, QueryParam<TRfqQuotationRecordParam> pageParam) {
        // 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
        if (Objects.isNull(pageParam)) {
            pageParam = new QueryParam<>();
        }
        if (Objects.isNull(pageParam.getParam())||Objects.isNull(pageParam.getParam().getQuotationBaseId())) {
            log.info("未传询报单ID");
            throw new ApiException(400,"未传询报单ID");
        }

        Optional<LoginInfo> infoOptional = SecurityUtils.getLoginInfo();
        if(infoOptional.isPresent()){
            LoginInfo loginInfo = infoOptional.get();
            String currentSubjectType = loginInfo.getCurrentSubjectType();
            if(SubjectType.sup.getCode().equals(currentSubjectType)){
                TRfqQuotationRecordParam param = pageParam.getParam();
                param.setSupplierId(loginInfo.getCurrentSupplier().getSupplierId());
            }
        }

        List<TRfqQuotationRecord> list = baseMapper.selectListPage(page, pageParam);
        List<TRfqQuotationRecordVo> voList = TRfqQuotationRecordWrapper.build().listVO(list);
        return page.setRecords(voList);
    }

    @Override
    public List<TRfqQuotationRecord> querylistOrderByTimes(TRfqQuotationRecord tRfqQuotationRecord, Boolean isAsc) {
        LambdaQueryWrapper<TRfqQuotationRecord> queryWrapper = Condition.getQueryWrapper(new TRfqQuotationRecord())
                .lambda()
                .eq(TRfqQuotationRecord::getQuotationBaseId, tRfqQuotationRecord.getQuotationBaseId())
                .eq(TRfqQuotationRecord::getSupplierId, tRfqQuotationRecord.getSupplierId());
        if (isAsc) {
            queryWrapper.orderByAsc(TRfqQuotationRecord::getRoundNumber);
        } else {
            queryWrapper.orderByDesc(TRfqQuotationRecord::getRoundNumber);
        }
        return super.list(queryWrapper);
    }

    @Override
    public List<TRfqQuotationRecord> querylistLately(Long quotationBaseId) {
        return baseMapper.querylistLately(quotationBaseId);
    }


}
