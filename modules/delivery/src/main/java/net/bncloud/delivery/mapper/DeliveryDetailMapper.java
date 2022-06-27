package net.bncloud.delivery.mapper;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bncloud.delivery.vo.DeliveryDetailVo;
import net.bncloud.delivery.param.DeliveryDetailParam;
import net.bncloud.serivce.api.order.dto.OrderDetailDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 送货明细表 Mapper 接口
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
public interface DeliveryDetailMapper extends BaseMapper<DeliveryDetail> {

    List<DeliveryDetail> selectListPage(IPage<DeliveryDetail> page,@Param("pageParam") QueryParam<DeliveryDetailParam> queryParam);

    void updateDetailErpId(List<DeliveryDetail> deliveryDetailList);

    DeliveryDetail getDetailById(Long id);

    OrderDetailDTO getMrpDetailById(Long deliveryPlanDetailItemId);

    /**
     * 查询在途数量
     * @param supplierCode
     * @param purchaseCode
     * @param materialCodeSet
     */
    List<DeliveryDetailVo> queryInTransitQuantity(@Param("supplierCode") String supplierCode, @Param("purchaseCode") String purchaseCode, @Param("materialCodeSet") Set<String> materialCodeSet);
}
