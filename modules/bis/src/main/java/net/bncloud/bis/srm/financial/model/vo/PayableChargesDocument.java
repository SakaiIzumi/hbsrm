package net.bncloud.bis.srm.financial.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.msk3cloud.core.fieldparse.FieldKey;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: liulu
 * @Date: 2022-02-14 10:32
 */
@ApiModel("对账-费用单")
@TableName(value = "sync_payable_charges_document")
@Data
public class PayableChargesDocument implements Serializable {

    private static final long serialVersionUID = -2877350031211962767L;

    /**
     * id值
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 实体主键
     */
    @FieldKey("FID")
    private Long fid;

    @ApiModelProperty("采购方编码")
    @FieldKey("FPURCHASEORGID")
    @JSONField(name = "FPURCHASEORGID")
    private String fPurchaseOrgId;

    @ApiModelProperty("供应商编码")
    @FieldKey("FSUPPLIERID")
    @JSONField(name = "FSUPPLIERID")
    private String fSupplierId;

    @ApiModelProperty("费用单号")
    @FieldKey("FBillNo")
    @JSONField(name = "FBillNo")
    private String fBillNo;

    @ApiModelProperty("发布时间")
    @FieldKey("FCreateDate")
    @JSONField(name = "FCreateDate")
    private LocalDateTime fCreateDate;

    @ApiModelProperty("发布人")
    @FieldKey("FCreatorId")
    @JSONField(name = "FCreatorId")
    private String fCreatorId;

    @ApiModelProperty("确认时间")
    @FieldKey("FAPPROVEDATE")
    @JSONField(name = "FAPPROVEDATE")
    private String fApproveDate;

    @ApiModelProperty("确认人")
    @FieldKey("FAPPROVERID")
    @JSONField(name = "FAPPROVERID")
    private String fApproverId;

    @ApiModelProperty("币别")
    @FieldKey("FCURRENCYID")
    @JSONField(name = "FCURRENCYID")
    private String fCurrencyId;

    @ApiModelProperty("税率")
    @FieldKey("FTaxRate")
    @JSONField(name = "FTaxRate")
    private String fTaxRate;

}
