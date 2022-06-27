package net.bncloud.quotation.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.NumberUtil;
import net.bncloud.quotation.entity.*;
import net.bncloud.quotation.enums.BiddingLineExtBizTypeEnum;
import net.bncloud.quotation.enums.QuotationDataType;
import net.bncloud.quotation.mapper.BiddingLineExtMapper;
import net.bncloud.quotation.mapper.QuotationLineExtMapper;
import net.bncloud.quotation.param.BiddingLineExtParam;
import net.bncloud.quotation.service.*;
import net.bncloud.quotation.utils.evalex.EvalexUtil;
import net.bncloud.quotation.vo.*;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 招标行信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BiddingLineExtServiceImpl extends BaseServiceImpl<BiddingLineExtMapper, BiddingLineExt> implements BiddingLineExtService {

    @Autowired
    private QuotationSupplierService quotationSupplierService;

    @Autowired
    private ITRfqQuotationRecordService tTRfqQuotationRecordService;

    @Autowired
    private QuotationLineExtService quotationLineExtService;

    @Autowired
    @Lazy
    private QuotationBaseService quotationBaseService;

    @Autowired
    private EvalexUtil evalexUtil;

    @Autowired
    private QuotationLineExtMapper quotationLineExtMapper;


    @Override
    public IPage<BiddingLineExt> selectPage(IPage<BiddingLineExt> page, QueryParam<BiddingLineExtParam> pageParam) {
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }

    @Override
    public List<BiddingLineExt> queryList(BiddingLineExt biddingLineExt) {
        LambdaQueryWrapper<BiddingLineExt> queryWrapper = Condition.getQueryWrapper(new BiddingLineExt())
                .lambda().eq(biddingLineExt.getQuotationBaseId() != null, BiddingLineExt::getQuotationBaseId, biddingLineExt.getQuotationBaseId())
                .eq(biddingLineExt.getSupplierId() != null, BiddingLineExt::getSupplierId, biddingLineExt.getSupplierId())
                .eq(biddingLineExt.getQuotationRecordId() != null, BiddingLineExt::getQuotationRecordId, biddingLineExt.getQuotationRecordId());
        return super.list(queryWrapper);
    }

    @Override
    public List<QuotationSupplierVo> quotationLineExtlist(Long quotationBaseId, Boolean isAll,List<String> supplierIds) {
        //查询询价单供应商
        QuotationSupplier quotationSupplier = new QuotationSupplier();
        quotationSupplier.setQuotationBaseId(quotationBaseId);
        List<QuotationSupplier> quotationSuppliers=new ArrayList<>();
        if(supplierIds==null){
            quotationSuppliers = quotationSupplierService.queryList(quotationSupplier,null);
        }else{
            for (String supplierId : supplierIds) {
                List<QuotationSupplier> quotationSuppliersBySupplierId = quotationSupplierService.queryList(quotationSupplier, Long.valueOf(supplierId));
                quotationSuppliers.addAll(quotationSuppliersBySupplierId);
            }
        }

        List<QuotationSupplierVo> allQuotationSupplierVos = new ArrayList();
        for (QuotationSupplier q : quotationSuppliers) {
            Long supplierId = q.getSupplierId();
            List<TRfqQuotationRecord> tRfqQuotationRecordQuerylist=null;
            if(supplierIds==null&&isAll){
                tRfqQuotationRecordQuerylist = getRecords(supplierId, quotationBaseId, true);
            }else{
                tRfqQuotationRecordQuerylist = getRecords(supplierId, quotationBaseId, false);
            }
            QuotationSupplierVo quotationSupplierVo = convertSpplierInfo(tRfqQuotationRecordQuerylist, q, quotationBaseId);
            allQuotationSupplierVos.add(quotationSupplierVo);
        }
        return allQuotationSupplierVos;
    }

    public List<TRfqQuotationRecord> getRecords(Long supplierId, Long quotationBaseId, Boolean isAll) {
        TRfqQuotationRecord tRfqQuotationRecord = new TRfqQuotationRecord();
        tRfqQuotationRecord.setQuotationBaseId(quotationBaseId);
        tRfqQuotationRecord.setSupplierId(supplierId);
        //查询所有报价记录
        if (isAll) {
            List<TRfqQuotationRecord> tRfqQuotationRecords = tTRfqQuotationRecordService.querylistOrderByTimes(tRfqQuotationRecord, true);
            return tRfqQuotationRecords;
        } else {
            //查询最新的报价记录
            List<TRfqQuotationRecord> tRfqQuotationRecords = tTRfqQuotationRecordService.querylistOrderByTimes(tRfqQuotationRecord, false);
            List<TRfqQuotationRecord> last = new ArrayList<>();
            if(!tRfqQuotationRecords.isEmpty()){
                last.add(tRfqQuotationRecords.get(0));
            }
            return last;
        }
    }

    public QuotationSupplierVo convertSpplierInfo(List<TRfqQuotationRecord> tRfqQuotationRecords, QuotationSupplier q, Long quotationBaseId) {
        QuotationSupplierVo quotationSupplierVo = new QuotationSupplierVo();
        BeanUtil.copyProperties(q, quotationSupplierVo);
        List<BiddingLineExt> allBiddingLineExt = new ArrayList();
        for (TRfqQuotationRecord tRfqQuotationRecord : tRfqQuotationRecords) {
            Long id = tRfqQuotationRecord.getId();
            BiddingLineExt biddingLineExt = new BiddingLineExt();
            biddingLineExt.setQuotationBaseId(quotationBaseId);
            biddingLineExt.setSupplierId(q.getSupplierId());
            biddingLineExt.setQuotationRecordId(id);
            List<BiddingLineExt> preData02 = queryList(biddingLineExt);
            allBiddingLineExt.addAll(preData02);
        }
        quotationSupplierVo.setAllBiddingLineExt(allBiddingLineExt);
        return quotationSupplierVo;
    }

    @Override
    public QuotationSupplierCompareVo quotationSupplierCompare(Long quotationBaseId) {
        QuotationLineExt quotationLineExt = new QuotationLineExt();
        quotationLineExt.setQuotationBaseId(quotationBaseId);
        List<QuotationLineExt> preData01 = quotationLineExtService.queryList(quotationLineExt);

        QuotationSupplierCompareVo quotationSupplierCompareVo = new QuotationSupplierCompareVo();
        quotationSupplierCompareVo.setFirstColumeData(sortDataAsc(preData01));

        //构造动态列数据
        List<QuotationSupplierVo> quotationSupplierVos = quotationLineExtlist(quotationBaseId, false, null);

        //排序，有报价的供应商排前面，没有的去掉就行
        List<QuotationSupplierVo> dynColumeData=new ArrayList<>();
        List<QuotationSupplierVo> collectForHaveData = quotationSupplierVos.stream().filter(item -> item.getAllBiddingLineExt().size() != 0 ).collect(Collectors.toList());
//        List<QuotationSupplierVo> collectForNoData = quotationSupplierVos.stream().filter(item -> item.getAllBiddingLineExt().size() ==0 ).collect(Collectors.toList());
        dynColumeData.addAll(collectForHaveData);
//        dynColumeData.addAll(collectForNoData);

        //quotationSupplierCompareVo.setDynColumeData(quotationLineExtlist(quotationBaseId, false,null));
        quotationSupplierCompareVo.setDynColumeData(dynColumeData);
        return quotationSupplierCompareVo;
    }


    public List<QuotationLineExt> sortDataAsc(List<QuotationLineExt> preData01) {
        Collections.sort(preData01, new Comparator<QuotationLineExt>() {
            @Override
            public int compare(QuotationLineExt o1, QuotationLineExt o2) {
                BigDecimal order01 = new BigDecimal(o1.getOrderValue());
                BigDecimal order02 = new BigDecimal(o2.getOrderValue());
                return order01.compareTo(order02);
            }
        });
        return preData01;
    }

    /**
     * 查询报价明细，方法过时，推荐使用 listByQuotationRecordId
     * @param biddingLineExt 报价行
     * @return 报价信息
     */
    @Override
    public List<BiddingLineExtVo> selectList(BiddingLineExt biddingLineExt) {

        //参数校验
        if (Objects.isNull(biddingLineExt) || biddingLineExt.getQuotationRecordId() == null) {
            log.info("未传报价记录ID");
            throw new ApiException(400, "未传报价记录ID");
        }

        //查询招标行信息列表
        List<BiddingLineExtVo> biddingLineExtVoList = baseMapper.selectBiddingLineList(biddingLineExt);
        Collections.sort(biddingLineExtVoList);


        //招标行信息列表校验

        if (biddingLineExtVoList.isEmpty()) {
            return new ArrayList<BiddingLineExtVo>();
        }


        //包装返回信息类
        List<BiddingLineExtVo> biddingLineExtVoListRes = biddingLineExtVoAdapter(biddingLineExtVoList);


        return biddingLineExtVoListRes;
    }

    private List<BiddingLineExtVo> biddingLineExtVoAdapter(List<BiddingLineExtVo> biddingLineExtVoList) {

        //查询询价行动态行扩展信息表
        Long quotationBaseId = biddingLineExtVoList.get(0).getQuotationBaseId();
        QuotationLineExt quotationLineExt = new QuotationLineExt();
        quotationLineExt.setQuotationBaseId(quotationBaseId);
        List<QuotationLineExtVo> quotationLineExtVoList = quotationLineExtService.selectQuotationLineExtlist(quotationLineExt);
        Collections.sort(quotationLineExtVoList);

        List<BiddingLineExtVo> biddingLineExtVoListRes = new ArrayList<>();

        QuotationLineExtVo total = new QuotationLineExtVo();
        total.setValue("合计");
        total.setOrderValue(quotationLineExtVoList.size() + 1 );
        quotationLineExtVoList.add(total);

        BiddingLineExtVo biddingLineExtVo;
        for (int i = 0; i < quotationLineExtVoList.size(); i++) {
            biddingLineExtVo = BeanUtil.copy(biddingLineExtVoList.get(i), BiddingLineExtVo.class);
            biddingLineExtVo.setQuotationLineExtValue(quotationLineExtVoList.get(i).getValue());
            biddingLineExtVo.setBiddingLineExtValue(biddingLineExtVoList.get(i).getValue());
            biddingLineExtVoListRes.add(biddingLineExtVo);
        }
        return biddingLineExtVoListRes;
    }

    @Override
    public List<BiddingLineExt> queryQuotationLastBidding(Long quotationId, Long supplierId, BiddingLineExtBizTypeEnum bizType,String dataType) {
        List<BiddingLineExt> biddingLineExtList = new ArrayList<>();

        QuotationBase quotationBase = quotationBaseService.getById(quotationId);
        Integer currentRoundNumber = quotationBase.getCurrentRoundNumber();
        Integer lastRoundNum = baseMapper.selectLastRoundNum(quotationId, supplierId, bizType,currentRoundNumber);
        if (lastRoundNum == null) {
            return biddingLineExtList;
        }
        QueryWrapper<BiddingLineExt> biddingLineExtQueryWrapper = Condition.getQueryWrapper(new BiddingLineExt());
        biddingLineExtQueryWrapper.lambda()
                .eq(BiddingLineExt::getQuotationBaseId, quotationId)
                .eq(BiddingLineExt::getSupplierId, supplierId)
                .eq(BiddingLineExt::getQuoteRound, lastRoundNum)
                .eq(BiddingLineExt::getBizType, bizType)
                .eq(BiddingLineExt::getDataType,dataType);
        return baseMapper.selectList(biddingLineExtQueryWrapper);
    }

    @Override
    public List<BiddingLineExt> queryQuotationExceptBidding(Long quotationId,  BiddingLineExtBizTypeEnum bizType,String dataType) throws Exception {
        QueryWrapper<BiddingLineExt> biddingLineExtQueryWrapper = Condition.getQueryWrapper(new BiddingLineExt());
        QuotationBase quotationBase = quotationBaseService.getById(quotationId);
        biddingLineExtQueryWrapper.lambda()
                .eq(BiddingLineExt::getQuotationBaseId, quotationId)
                .eq(BiddingLineExt::getQuoteRound, quotationBase.getCurrentRoundNumber()-1)
                .eq(BiddingLineExt::getBizType, bizType)
                .eq(BiddingLineExt::getDataType,dataType);
        return baseMapper.selectList(biddingLineExtQueryWrapper);
    }

    @Override
    public List<BiddingLineExtVo> cheapest(Long quotationBaseId) {

        List<BiddingLineExtVo> biddingLineExtList = new ArrayList<>();

        // 验证/查询 询价单信息
        checkoutQuotationBase(quotationBaseId);
        /*//查出最低报价
        List<BiddingLineExtVo> biddingLineExtVosRes = cheapestBiddingLineExt(quotationBaseId);
        //`计算公式`计算出结果
        calculateBiddingLineExtVos(quotationBaseId, biddingLineExtVosRes);*/
        QuotationLineExt quotationLineExt = new QuotationLineExt();
        quotationLineExt.setQuotationBaseId(quotationBaseId);
        List<QuotationLineExt> quotationLineExtList = quotationLineExtService.queryList(quotationLineExt);
        if(CollectionUtils.isNotEmpty(quotationLineExtList)){
            List<BiddingLineExtVo> biddingLineExtVos = quotationLineExtList.stream().map(item -> {
                BiddingLineExtVo biddingLineExtVo = BeanUtil.copyProperties(item, BiddingLineExtVo.class);
                assert biddingLineExtVo != null;
                biddingLineExtVo.setQuotationLineExtValue(biddingLineExtVo.getTitle());
                String dataType = biddingLineExtVo.getDataType();
                if (QuotationDataType.normal.name().equals(dataType)) {
                    String extValue="extValue";
                    extValue=StrUtil.isBlank(biddingLineExtVo.getValue())? 0+"" : biddingLineExtVo.getValue();

                    biddingLineExtVo.setBiddingLineExtValue(extValue);
                } else {
                    biddingLineExtVo.setBiddingLineExtValue(biddingLineExtVo.getExpressionValue() == null ? null : biddingLineExtVo.getExpressionValue().toString());
                }
                return biddingLineExtVo;
            }).collect(Collectors.toList());


            //设置最低价
            buildMinAmountLineExtList(biddingLineExtVos,quotationBaseId);

            //`计算公式`计算出结果
            calculateBiddingLineExtVos(quotationBaseId, biddingLineExtVos);

            return biddingLineExtVos;

        }
        return biddingLineExtList;
    }

    /**
     * 构建包含最小金额信息的报价信息
     * @param biddingLineExtVos 询价单行默认信息
     * @param quotationBaseId  询价单ID
     */
    private void buildMinAmountLineExtList(List<BiddingLineExtVo> biddingLineExtVos, Long quotationBaseId) {
        List<QuotationMinAmount> quotationMinAmountList = queryMinAmount(quotationBaseId);
        if(CollectionUtils.isNotEmpty(biddingLineExtVos) && CollectionUtils.isNotEmpty(quotationMinAmountList)){
            for (QuotationMinAmount quotationMinAmount : quotationMinAmountList) {
                for (BiddingLineExtVo biddingLineExtVo : biddingLineExtVos) {
                    //金额字段field
                    String filed = quotationMinAmount.getField();
                    if (toReset(filed,biddingLineExtVo)) {
                        biddingLineExtVo.setBiddingLineExtValue(quotationMinAmount.getMinAmount()==null ? null : quotationMinAmount.getMinAmount().toString());
                        break;
                    }
                }
            }
        }

    }

    /**
     * 是否 重置为最小金额
     * @param filed 字段名称
     * @param biddingLineExtVo 询价行信息
     * @return TRUE 是，FALSE 否
     */
    private boolean toReset(String filed, BiddingLineExtVo biddingLineExtVo) {
        String dataType = biddingLineExtVo.getDataType();
        return StringUtils.isNotBlank(filed) && filed.equals(biddingLineExtVo.getField()) && QuotationDataType.normal.name().equals(dataType);
    }

    @Override
    public void saveRestateBiddingLineExt(Long quotationBaseId, Integer currentRoundNumber) {
        List<BiddingLineExtVo> cheapest = cheapest(quotationBaseId);
        List<BiddingLineExt> biddingLineExts = new ArrayList<>();

        Date now = DateUtil.date();
        Long userId = AuthUtil.getUser().getUserId();
        for (BiddingLineExtVo cheap : cheapest) {
            BiddingLineExt biddingLineExt = new BiddingLineExt();
            BeanUtil.copyProperties(cheap, biddingLineExt,"id");
            biddingLineExt.setValue(cheap.getBiddingLineExtValue());
            biddingLineExt.setBizType(BiddingLineExtBizTypeEnum.EXCEPT.name());
            biddingLineExt.setSupplierId(null);
            biddingLineExt.setQuotationRecordId(null);
            biddingLineExt.setQuoteRound(currentRoundNumber.toString());
            biddingLineExt.setCreatedBy(userId);
            biddingLineExt.setCreatedDate(now);
            biddingLineExt.setLastModifiedBy(userId);
            biddingLineExt.setLastModifiedDate(now);
            biddingLineExt.setIsDeleted(0);
            biddingLineExts.add(biddingLineExt);

        }

        this.saveBatch(biddingLineExts);

    }

    /**
     * 根据报价记录ID ,查询报价信息
     * @param quotationRecordId 报价记录ID
     * @return
     */
    @Override
    public List<BiddingLineExt> listByQuotationRecordId(Long quotationRecordId) {
        BiddingLineExt biddingLineExt = new BiddingLineExt();
        biddingLineExt.setQuotationRecordId(quotationRecordId);
        LambdaQueryWrapper<BiddingLineExt> queryWrapper = Condition.getQueryWrapper(biddingLineExt).lambda().orderByAsc(BiddingLineExt::getOrderValue);
        return super.list(queryWrapper);
    }

    /**
     * 计算出结果
     *
     * @param quotationBaseId
     * @param biddingLineExtVosRes
     * @return
     */
    private List<BiddingLineExtVo> calculateBiddingLineExtVos(Long quotationBaseId, List<BiddingLineExtVo> biddingLineExtVosRes) {

        //查出询价行动态行扩展信息 字段类型为计算公式的信息
        QuotationLineExt quotationLineExt = new QuotationLineExt();
        quotationLineExt.setQuotationBaseId(quotationBaseId);
        quotationLineExt.setDataType("expression");
        List<QuotationLineExtVo> quotationLineExtVos = quotationLineExtMapper.selectQuotationLineExtlist(quotationLineExt);

        quotationLineExtVos.forEach(item->{
            if(StrUtil.isEmpty(item.getExpression()) ){
                item.setExpression(0+"");
            }
        });



        //存放报价信息，biddingLineExtVosRes到map
        Map<String, BiddingLineExtVo> quotationLineExtVosMap = new HashMap<>(16);
        for (BiddingLineExtVo biddingLineExtVo : biddingLineExtVosRes) {
            quotationLineExtVosMap.put(biddingLineExtVo.getField(), biddingLineExtVo);
        }

        //根据`计算公式`生成`计算语句`，计算出结果
        BigDecimal calculate;
        BiddingLineExtVo biddingLineExtVoTemp;

        //遍历公式字段，计算结果值
        for (QuotationLineExtVo quotationLineExtVo : quotationLineExtVos) {
            calculate = evalexUtil.calculate(quotationLineExtVo.getFormula(), biddingLineExtVosRes);
            //从map中获取biddingLineExtVo，设置计算后的值
            biddingLineExtVoTemp = quotationLineExtVosMap.get(quotationLineExtVo.getField());
            if (Objects.nonNull(biddingLineExtVoTemp)) {
                biddingLineExtVoTemp.setBiddingLineExtValue(calculate.toEngineeringString());
                biddingLineExtVoTemp.setExpressionValue(calculate);
            }
        }
        return biddingLineExtVosRes;
    }

    /**
     * 查出最低报价
     *
     * @param quotationBaseId
     */
    private List<BiddingLineExtVo> cheapestBiddingLineExt(Long quotationBaseId) {
        //查出所有供应商的最新报价
        List<TRfqQuotationRecord> tRfqQuotationRecords = tTRfqQuotationRecordService.querylistLately(quotationBaseId);
        if(tRfqQuotationRecords.isEmpty()){
            throw new ApiException(400,"没有供应商报价");
        }
        //查出最低报价
        List<BiddingLineExtVo> biddingLineExtVos = baseMapper.queryCheapest(tRfqQuotationRecords);
        Collections.sort(biddingLineExtVos);
        //设置返回信息
        List<BiddingLineExtVo> biddingLineExtVosRes = biddingLineExtVoAdapter(biddingLineExtVos);

        return biddingLineExtVosRes;
    }

    /**
     * 查询最低报价
     * @param quotationBaseId 询价单基础信息ID
     * @return 最低金额字段信息
     */
    private List<QuotationMinAmount> queryMinAmount(Long quotationBaseId){
        //查出所有供应商的最新报价
        List<TRfqQuotationRecord> tRfqQuotationRecords = tTRfqQuotationRecordService.querylistLately(quotationBaseId);
        if(CollectionUtils.isEmpty(tRfqQuotationRecords)){
            return Collections.emptyList();
        }
        List quotationRecordIdList = tRfqQuotationRecords.stream().map(TRfqQuotationRecord::getId).collect(Collectors.toList());
        return baseMapper.queryMinAmount(quotationRecordIdList);

    }

    /**
     * // 验证/查询 询价单信息
     *
     * @param quotationBaseId
     */
    private QuotationBase checkoutQuotationBase(Long quotationBaseId) {
        QuotationBase quotationBase = quotationBaseService.getById(quotationBaseId);
        if (Objects.isNull(quotationBase)) {
            log.info("询价单信息不存在");
            throw new ApiException(ResultCode.FAILURE.getCode(), "询价单信息不存在");
        }
        return quotationBase;
    }


}
