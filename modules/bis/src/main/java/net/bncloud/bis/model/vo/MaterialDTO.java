package net.bncloud.bis.model.vo;

import lombok.Data;
import lombok.ToString;
import net.bncloud.msk3cloud.core.fieldparse.FieldKey;

/**
 * 废弃 直接使用了net.bncloud.quotation.service.api.dto.MaterialInfoDTO
 * @author lijiaju
 * @date 2022/2/21 16:52
 */
@Data
@ToString
@Deprecated
public class MaterialDTO {
//    FMATERIALID,FName,FNumber,FSpecification,FBARCODE,FMaterialGroup.fname,F_MS_WLFL.fdatavalue,FErpClsID,FSuite,FBaseUnitId.fname,FTaxRateId.fname,FCategoryID.fname,
//    FTaxType.fdatavalue,FPerUnitStandHour,FIssueType,F_MS_PP
    //物料内码
    @FieldKey("FMATERIALID")
    private Long fMATERIALID;
    //名称
    @FieldKey("FName")
    private String fName;
    //编码
    @FieldKey("FNumber")
    private String fNumber;
    //规格型号
    @FieldKey("FSpecification")
    private String fSpecification;
    //条码
    @FieldKey("FBARCODE")
    private String fBARCODE;
    //物料分组
    @FieldKey("FMaterialGroup.fname")
    private String fMaterialGroup;
    //物料分类
    @FieldKey("F_MS_WLFL.fdatavalue")
    private String fMsWlfl;
    //物料属性
    @FieldKey("FErpClsID")
    private String fErpClsId;
    //套件
    @FieldKey("FSuite")
    private String fSuite;
    //基本单位
    @FieldKey("FBaseUnitId.fname")
    private String fBaseUnitId;
    //默认税率
    @FieldKey("FTaxRateId.fname")
    private String fTaxRateId;
    //存货类别
    @FieldKey("FCategoryID.fname")
    private String fCategoryID;
    //税分类
    @FieldKey("FTaxType.fdatavalue")
    private String fTaxType;
    //标准工时
    @FieldKey("FPerUnitStandHour")
    private String fPerUnitStandHour;
    //发料方式
    @FieldKey("FIssueType")
    private String fIssueType;
    //品牌
    @FieldKey("F_MS_PP")
    private String fMsPp;


}
