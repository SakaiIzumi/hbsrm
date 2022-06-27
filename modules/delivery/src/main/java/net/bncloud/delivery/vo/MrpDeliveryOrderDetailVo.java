package net.bncloud.delivery.vo;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.web.jackson.AmountSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MrpDeliveryOrderDetailVo  implements Serializable {


    private static final long serialVersionUID = -4127265045483817754L;


    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    //deliveryPlanDetailItemId


    /**
     * 采购单号
     */
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderCode;

    /**
     * 项次
     */
    @ApiModelProperty(value = "项次")
    private String itemCode;


    /**
     * 商家编码（条码）
     */
    @ApiModelProperty(value = "商家编码（条码）")
    private String merchantCode;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码")
    private String productCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**
     * 产品规格
     */
    @ApiModelProperty(value = "产品规格")
    private String productSpecs;

    //productSpecifications

    /**
     * 收货部门名称
     */
    @ApiModelProperty(value = "收货部门名称")
    private String takeOverDepartmentName;

    /**
     * 收货部门ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "收货部门ID")
    private Long takeOverDepartmentId;

    /**
     * 收货仓库
     */
    @ApiModelProperty(value = "收货仓库")
    private String takeOverWarehouse;

    /**
     * 采购说明
     */
    @ApiModelProperty(value = "采购说明")
    private String purchaseRemarks;

    /**
     * 交货日期
     */
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME, timezone="GMT+8")
    @ApiModelProperty(value = "交货日期")
    private Date deliveryTime;

    /**
     * 交货方式
     */
    @ApiModelProperty(value = "交货方式")
    private String deliveryType;

    /**
     * 答交状态
     */
    @ApiModelProperty(value = "答交状态")
    private Integer status;

    /**
     * 采购数量
     */
    @ApiModelProperty(value = "采购数量")
    private BigDecimal purchaseNum;

    /**
     * 采购单位
     */
    @ApiModelProperty(value = "采购单位")
    private String purchaseUnit;

    /**
     * 采购编码
     */
    @ApiModelProperty(value = "采购编码")
    private String purchaseCode;

    /**
     * 已发数量
     */
    @ApiModelProperty(value = "已发数量")
    private BigDecimal sendNum;

    /**
     * 收货状态 1）待收货
     * 2）部分收货
     * 3）收货完成
     * 4）已结案
     */
    @ApiModelProperty(value = "发货状态")
    private String takeOverStatus;

    /**
     * 计价数量
     */
    @ApiModelProperty(value = "计价数量")
    private BigDecimal markDownNum;

    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal unitPrice;

    /**
     * 产品总价
     */
    @ApiModelProperty(value = "产品总价")
    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal productTotalPrice;

    /**
     * 供应商备注
     */
    @ApiModelProperty(value = "供应商备注")
    private String supplierRemarks;

    /**
     * 品牌方备注
     */
    @ApiModelProperty(value = "品牌方备注")
    private String brandRemarks;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private String files;

    /**
     * 计价单位
     */
    @ApiModelProperty(value = "计价单位")
    private String valuationUnit;



    /**
     * 美尚 含税单价
     */
    @ApiModelProperty(value = "含税单价")
    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal taxPrice;

    /**
     * 美尚 税率
     */
    @ApiModelProperty(value = "税率")
    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal taxRate;

    /**
     * 美尚 税额
     */
    @ApiModelProperty(value = "税额")
    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal taxAmount;

    /**
     * 美尚 税价合计
     */
    @ApiModelProperty(value = "税价合计")
    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal allAmount;

    /**
     * 美尚 变更标志
     */
    @ApiModelProperty(value = "变更标志")
    private String changeCode;

    /**
     * 入库数量
     */
    private BigDecimal inventoryQuantity;

    /**
     * 来源系统主键ID
     */
    @ApiModelProperty(value = "来源系统主键ID")
    private String sourceId;
    //planDetailItemSourceId

    /**
     * 计划单位
     */
    @ApiModelProperty(value = "计划单位")
    private String planUnit;

    /**
     * 剩余数量
     */
    @ApiModelProperty(value = "剩余数量")
    private String remainingQuantity;

    /**
     * 送货数量
     */
    @ApiModelProperty(value = "送货数量")
    private String deliveryQuantity;
    //planQuantity

    /**
     * 是不是mrp同步过来的订单
     */
    @ApiModelProperty(value = "是不是mrp同步过来的订单")
    private String mrpStatus;

    /**
     * 计划编号
     */
    @ApiModelProperty(value = "计划编号")
    private String planNo;

    /**
     * 计划编号 (上面已经有字段,适应前端,再设置一次)
     */
    @ApiModelProperty(value = "计划编号")
    private String deliveryPlanNo;

    //deliveryPlanId  订单的id，如果需要可以设置
    //deliveryNoteId  送货单id
    //orderType 在采购订单里面
    //deliveryNoteNo  送货单号
    //deliveryStatus 送货状态（送货项次表写着已弃用）


    /**
     * 产品规格 (上面已经有字段,适应前端,再设置一次)
     */
    @ApiModelProperty(value = "产品规格")
    private String productSpecifications;

    /**
     * 订单编号 (purchaseOrderCode  上面已经有字段,适应前端,再设置一次)
     */
    @ApiModelProperty(value = "订单编号")
    private String billNo;

    /**
     * 单据编号 (这个是采购订单的orderNo )
     */
    @ApiModelProperty(value = "单据编号")
    private String purchaseOrderNo;

    /**
     * 送货日期 (delivery_time 字段 适应前端,再设置一次 )
     */
    @ApiModelProperty(value = "单据编号")
    private Date deliveryDate;

    /**
     * 送货地址
     */
    @ApiModelProperty(value = "送货地址")
    private String deliveryAddress;



    /**
     * 送货明细
     */
    private List<DeliveryDetailVo> deliveryDetailList;

    /**
     * 添加剩余可发货数量
     */
    public void addRemainingQuantity( BigDecimal quantity){
        String remainingQuantity = this.getRemainingQuantity();
        BigDecimal remainingQuantityBigDecimal = StrUtil.isNotBlank(remainingQuantity) ? new BigDecimal(remainingQuantity) : new BigDecimal("0");
        BigDecimal add = remainingQuantityBigDecimal.add(quantity);
        this.setRemainingQuantity( add.toString() );
    }


}
