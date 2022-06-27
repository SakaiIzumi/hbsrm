package net.bncloud.oem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.oem.domain.entity.PurchaseOrder;
import net.bncloud.oem.domain.param.PurchaseOrderParam;
import net.bncloud.oem.domain.vo.PurchaseOrderVo;
import net.bncloud.oem.domain.vo.ReturnedReceiptsVo;
import net.bncloud.oem.domain.vo.ToBeConfirmVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ddh
 * @since 2022/4/24
 * @description
 */
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {

    List<PurchaseOrderVo> selectPage(IPage<PurchaseOrderVo> page, @Param("queryParam") QueryParam<PurchaseOrderParam> queryParam);

    List<PurchaseOrderVo> selectOemSupplierPageList(IPage<PurchaseOrderVo> page, @Param("queryParam") QueryParam<PurchaseOrderParam> queryParam);

    List<ToBeConfirmVo> selectToBeConfirmList(@Param("page")IPage<ToBeConfirmVo> page,@Param("queryParam") QueryParam<PurchaseOrderParam> queryParam);

    List<ReturnedReceiptsVo> selectReturnedReceiptPage(IPage<ReturnedReceiptsVo> page,@Param("queryParam") QueryParam<PurchaseOrderParam> queryParam);

    ReturnedReceiptsVo getReturnedReceiptById(@Param("id") Long id);


}