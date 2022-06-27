package net.bncloud.delivery.mapper;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryNote;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.delivery.enums.DeliveryStatusEnum;
import net.bncloud.delivery.param.DeliveryNoteParam;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
/**
 * <p>
 * 送货单信息表 Mapper 接口
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
public interface DeliveryNoteMapper extends BaseMapper<DeliveryNote> {

    /**
     * 分页列表查询
     * @param page
     * @param queryParam
     * @return
     */
    List<DeliveryNote> selectListPage(IPage page, @Param("queryParam") QueryParam<DeliveryNoteParam> queryParam);

    Integer selectByNotSign();

    void updateErpId(@Param("id") Long id ,@Param("erpId")Long erpId,@Param("fNumber")String fNumber,@Param("code")String code);

    void updateStatus(Long id);

    void updateSync(@Param("deliveryStatusCode")String deliveryStatusCode,
                    @Param("erpSigningStatus")String erpSigningStatus,
                    @Param("signingTime") Date signingTime,
                    @Param("id")Long id);
}
