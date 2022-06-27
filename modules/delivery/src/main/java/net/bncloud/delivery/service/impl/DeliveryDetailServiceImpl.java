package net.bncloud.delivery.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.constant.DeliveryDetailExcelImportConstants;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.entity.DeliveryNoteExcelImportDetailVo;
import net.bncloud.delivery.mapper.DeliveryDetailMapper;
import net.bncloud.delivery.param.DeliveryDetailParam;
import net.bncloud.delivery.service.DeliveryDetailService;
import net.bncloud.delivery.service.DeliveryPlanDetailItemMatcher;
import net.bncloud.delivery.utils.BeanListCopyUtil;
import net.bncloud.delivery.utils.DeliveryDetailUtils;
import net.bncloud.delivery.vo.DeliveryDetailExcelImportResult;
import net.bncloud.delivery.vo.DeliveryDetailVo;
import net.bncloud.delivery.vo.PrintDataVo;
import net.bncloud.delivery.vo.ShippableDeliveryPlanDetailItemVo;
import net.bncloud.serivce.api.order.dto.OrderDetailDTO;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 送货明细表 服务实现类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Slf4j
@Service
public class DeliveryDetailServiceImpl extends BaseServiceImpl<DeliveryDetailMapper, DeliveryDetail> implements DeliveryDetailService {

    @Resource
    private DeliveryDetailMapper deliveryDetailMapper;

    @Lazy
    @Autowired
    private DeliveryPlanDetailItemMatcher deliveryPlanDetailItemMatcher;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 保存送货明细
     *
     * @param
     * @return
     */
    @Override
    public List<DeliveryDetailVo> saveDetails(Long deliveryId ,List<DeliveryDetailVo> detailVos) {
        AtomicInteger itemNo = new AtomicInteger();
        detailVos.forEach(vo->{
            DeliveryDetail detail = BeanUtil.copy(vo, DeliveryDetail.class);
            detail.setId(null);
            detail.setDeliveryId(deliveryId);
            detail.setItemNo(itemNo.incrementAndGet());
            save(detail);
            vo.setId(detail.getId());
        });
        return detailVos;
    }

    @Override
    public List<DeliveryDetail> getDeliveryDetailList(Pageable pageable, QueryParam<DeliveryDetailParam> queryParam) {
        List<DeliveryDetail> deliveryDetails = baseMapper.selectListPage(PageUtils.toPage(pageable), queryParam);
        return deliveryDetails;
    }

    @Override
    public PrintDataVo<DeliveryDetailVo> printData(QueryParam<DeliveryDetailParam> queryParam) {
        List<DeliveryDetail> deliveryDetails = deliveryDetailMapper.selectListPage(null, queryParam);
        List<DeliveryDetailVo> deliveryDetailVos = BeanListCopyUtil.copyListProperties(deliveryDetails, DeliveryDetailVo::new);
        PrintDataVo<DeliveryDetailVo> vo = new PrintDataVo<>();
        vo.setData(deliveryDetailVos);
        if (SecurityUtils.getLoginInfo().isPresent()) {
            //LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            //vo.setPrintBy(loginInfo.getName());
            //vo.setPrintDate(new Date());
            //vo.setSupplier(loginInfo.getCurrentSupplier().getSupplierName());
        } else {
            log.warn("获取用户登录信息失败");
        }
        return vo;
    }

    @Override
    public void updateDetailErpId(List<DeliveryDetail> deliveryDetailList) {
        deliveryDetailMapper.updateDetailErpId(deliveryDetailList);
    }

    @Override
    public DeliveryDetail getDetailById(Long id) {
        return deliveryDetailMapper.getDetailById(id);
    }

    /**
     * @param deliveryNoteExcelImportDetailVoList
     * @param deliveryNote
     * @param productCodeShippableDeliveryPlanDetailItemListMap
     * @param ratioOfSentProportion
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void matchImportExcelData(List<DeliveryNoteExcelImportDetailVo> deliveryNoteExcelImportDetailVoList,
                                     DeliveryNote deliveryNote, Map<String,
                                     List<ShippableDeliveryPlanDetailItemVo>> productCodeShippableDeliveryPlanDetailItemListMap,
                                     BigDecimal ratioOfSentProportion ) {

        // 依据产品编码分组数据
        RMap<Long, DeliveryDetailExcelImportResult> deliveryDetailExcelImportRedissonMap = redissonClient.getMap(DeliveryDetailExcelImportConstants.DELIVERY_DETAIL_IMPORT_EXCEL_MAP_KEY, JsonJacksonCodec.INSTANCE);
        Long deliveryNoteId = deliveryNote.getId();
        DeliveryDetailExcelImportResult deliveryDetailExcelImportResult = deliveryDetailExcelImportRedissonMap.get(deliveryNoteId);
        try {
            log.info("[matchImportExcelData] start handle! ");

            deliveryNoteExcelImportDetailVoList.stream().collect( Collectors.groupingBy( DeliveryNoteExcelImportDetailVo::getProductCode )).forEach( (productCode,groupByDeliveryNoteExcelImportDetailVoList ) -> {
                // 查询需要处理的项次
                List<ShippableDeliveryPlanDetailItemVo> shippableDeliveryPlanDetailItemVos = productCodeShippableDeliveryPlanDetailItemListMap.get(productCode);
                // 计算每个项次可超出的值
                int remainingQuantityTotal = shippableDeliveryPlanDetailItemVos.stream().mapToInt( shippableDeliveryPlanDetailItemVo -> Integer.parseInt(shippableDeliveryPlanDetailItemVo.getRemainingQuantity())).sum();
                long realDeliveryQuantity = groupByDeliveryNoteExcelImportDetailVoList.stream().mapToLong(DeliveryNoteExcelImportDetailVo::getRealDeliveryQuantity).sum();
                int excessValueTotal = DeliveryDetailUtils.remainingQuantityNeedToOverSend(remainingQuantityTotal, ratioOfSentProportion, realDeliveryQuantity);
                if ( excessValueTotal > 0) {
                    // 每一个项次可分配的值
                    DeliveryDetailUtils.allocateExcessValueTotalToDeliveryPlanDetailItemList(excessValueTotal,shippableDeliveryPlanDetailItemVos  );
                }

                deliveryPlanDetailItemMatcher.matchDeliveryDetailWithDeliveryNoteExcelImportDetailVoList( groupByDeliveryNoteExcelImportDetailVoList,deliveryNote,shippableDeliveryPlanDetailItemVos );
            });

            deliveryDetailExcelImportResult.setStatus( DeliveryDetailExcelImportResult.SUCCESS );
            deliveryDetailExcelImportRedissonMap.put( deliveryNoteId, deliveryDetailExcelImportResult);
        } catch (Exception ex){
            log.warn("参数信息：deliveryNoteExcelImportDetailVoList:{},deliveryNote:{},productCodeShippableDeliveryPlanDetailItemListMap:{}", JSON.toJSONString( deliveryNoteExcelImportDetailVoList ),JSON.toJSONString( deliveryNote ),JSON.toJSONString( productCodeShippableDeliveryPlanDetailItemListMap )  );
            log.error("[matchImportExcelData] handle data error! ",ex);

            deliveryDetailExcelImportResult.setStatus( DeliveryDetailExcelImportResult.FAIL );
            deliveryDetailExcelImportResult.setRemark( ex.getMessage() );
            deliveryDetailExcelImportRedissonMap.put( deliveryNoteId, deliveryDetailExcelImportResult );
        }

    }

    @Override
    public OrderDetailDTO getMrpDetailById(Long deliveryPlanDetailItemId) {
        return deliveryDetailMapper.getMrpDetailById(deliveryPlanDetailItemId);
    }

    @Override
    public List<DeliveryDetailVo> queryInTransitQuantity(String supplierCode, String purchaseCode, Set<String> materialCodeSet) {

        return this.baseMapper.queryInTransitQuantity(supplierCode, purchaseCode, materialCodeSet);


    }
}
