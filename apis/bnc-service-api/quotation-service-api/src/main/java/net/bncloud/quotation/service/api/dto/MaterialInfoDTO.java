package net.bncloud.quotation.service.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.bncloud.msk3cloud.core.fieldparse.FieldKey;

import java.util.HashMap;
import java.util.Map;

/**

 * 1	物料名称	ERP		物料	FBillHead 	名称	FName  (必填项)
 * 2	物料编码	ERP		物料	FBillHead 	编码	FNumber
 * 3	物料规格	ERP		物料	FBillHead 	规格型号	FSpecification
 * 4	物料条码	ERP		基本	SubHeadEntity 	条码	FBARCODE
 * 5	物料分组	ERP		物料	FBillHead 	物料分组	FMaterialGroup
 * 6	物料分类	ERP		物料	FBillHead 	物料分类	F_MS_WLFL  (必填项)
 * 7	物料属性	ERP		基本	SubHeadEntity 	物料属性	FErpClsID  (必填项)
 * 8	是否套件	ERP		基本	SubHeadEntity 	套件	FSuite  (必填项)
 * 9	基本单位	ERP		基本	SubHeadEntity 	基本单位	FBaseUnitId  (必填项)
 * 10	默认税率	ERP		基本	SubHeadEntity 	默认税率	FTaxRateId
 * 11	存货类别	ERP		基本	SubHeadEntity 	存货类别	FCategoryID  (必填项)
 * 12	税分类	ERP		基本	SubHeadEntity 	税分类	FTaxType
 * 13	来源	ERP				-
 * 14	替代物料	ERP				-
 * 15	标准工时	ERP		生产	SubHeadEntity5 	标准工时	FPerUnitStandHour
 * 16	发料方式	ERP		生产	SubHeadEntity5 	发料方式	FIssueType  (必填项)
 * 17	品牌			物料	FBillHead 	品牌	F_MS_PP  (必填项)
 * 同步feign参数类 因为之前的entity放在了quotation模块 直接复制一份quotation模块的对象出来了
 * @author lijiaju
 * @date 2022/2/22 9:24
 */
@Data
@ToString
public class MaterialInfoDTO {
    public static final String MATERIAL_PROPERTY_ENUM_KEY = "MATERIAL_PROPERTY_ENUM_KEY";
    public static final String MATERIAL_SUITE_ENUM_KEY = "MATERIAL_SUITE_ENUM_KEY";
    public static final String MATERIAL_ISSUE_TYPE_ENUM_KEY = "MATERIAL_ISSUE_TYPE_ENUM_KEY";
    private static final long serialVersionUID = 1L;
//    FMATERIALID,FName,FNumber,FSpecification,FBARCODE,FMaterialGroup.fname,F_MS_WLFL.fdatavalue,FErpClsID,FSuite,FBaseUnitId.fname,FTaxRateId.fname,\
//    FCategoryID.fname,FTaxType.fdatavalue,FPerUnitStandHour,FIssueType,F_MS_PP.fdatavalue
    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码")
    @FieldKey("FNumber")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    @FieldKey("FName")
    private String materialName;

    /**
     * ISV物料编码
     */
    @ApiModelProperty(value = "条码")
    @FieldKey("FBARCODE")
    private String barCode;

    /**
     * 分类编码Id
     */
    @ApiModelProperty(value = "分类编码Id")
    @FieldKey("F_MS_WLFL")
    private String genreCodeId;


    /**
     * 分类编码
     */
    @ApiModelProperty(value = "分类编码Id")
    @FieldKey("F_MS_WLFL.fnumber")
    private String genreCode;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    @FieldKey("F_MS_WLFL.fdatavalue")
    private String genreName;

    /**
     * 物料规格
     */
    @ApiModelProperty(value = "物料规格")
    @FieldKey("FSpecification")
    private String special;

    /**
     * 物料描述
     */
    @ApiModelProperty(value = "物料描述")
//    @FieldKey("")
    private String description;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    @FieldKey("FBaseUnitId.fname")
    private String unit;

    /**
     * 来源类型
     */
    @ApiModelProperty(value = "来源类型")
//    @FieldKey("")
    private String sourceType;

    /**
     * 来源名称
     */
    @ApiModelProperty(value = "来源名称")
//    @FieldKey("")
    private String sourceName;

    /**
     * 来源系统ID
     */
    @ApiModelProperty(value = "来源系统ID")
    @FieldKey("FMATERIALID")
    private Long sourceId;

    @ApiModelProperty(value = "品牌")
    @FieldKey("F_MS_PP.fdatavalue")
    private String brand;

    /**
     */
    @ApiModelProperty(value = "物料属性")
    @FieldKey("FErpClsID")
    private String property;

    @ApiModelProperty("物料分组名称")
    @FieldKey("FMaterialGroup.fname")
    private String materialGroupName;


    @ApiModelProperty("物料分组编码")
    @FieldKey("FMaterialGroup")
    private Integer materialGroupId;

    @ApiModelProperty("标准工时")
    @FieldKey("FPerUnitStandHour")
    private String unitStandHour;

    @ApiModelProperty("是否套件")
    @FieldKey("FSuite")
    private String suite;

    @ApiModelProperty("发料方式")
    @FieldKey("FIssueType")
    private String issueType;

    @ApiModelProperty("税分类")
    @FieldKey("FTaxType.fdatavalue")
    private String taxType;

    @ApiModelProperty("默认税率")
    @FieldKey("FTaxRateId.fname")
    private String taxRate;

    @ApiModelProperty("是否有效")
    @FieldKey("FForbidStatus")
    private String forbiddenStatus;
}
