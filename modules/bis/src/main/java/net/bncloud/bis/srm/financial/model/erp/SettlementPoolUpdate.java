package net.bncloud.bis.srm.financial.model.erp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.msk3cloud.core.fieldparse.FieldKey;

import java.io.Serializable;

/**
 * @author: liulu
 * @Date: 2022-02-14 10:32
 */
@ApiModel("结算池对象")
@TableName(value = "settlement_pool_sync")
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SettlementPoolUpdate implements Serializable {

    private static final long serialVersionUID = -2877350031211962767L;

    /**
     * id值
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 抬头实体主键
     */
    @SerializedName("FID")
    @FieldKey("FID")
    private Long fid;

    /**
     * srm对账单状态
     */
    @SerializedName("F_srm_dzzt")
    @FieldKey("F_srm_dzzt")
    private String fSrmDzzt;


    @ApiModelProperty("费用单号")
    @FieldKey("FBillNo")
    private String fBillNo;

}
