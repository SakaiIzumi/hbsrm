package net.bncloud.bis.model.oa;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author jabngit frame
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UfSrmContract对象", description="")
public class UfSrmContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField("requestId")
    private Integer requestId;

    private Integer sqr;

    private Integer szbm;

    private String sqrq;

    private Integer sfxq;

    private Integer hzflx;

    private String wffzr;

    private String dffzr;

    private Integer qdlx;

    private Integer sfsw;

    private Integer bz;

    private BigDecimal htje;

    private Integer sfhs;

    private String sm;

    private String xgfj;

    private Integer htfxjb;

    private String beizhu;

    private String lcbh;

    private String wfdwmc;

    private String szpp;

    private Integer manager;

    private Integer bmc;

    private String dfdwmc;

    private String hth;

    private String zxcsr;

    private Integer nfkp;

    private Integer nsrzz;

    private Integer fplx;

    private Integer kpqk;

    private String zq;

    private String dhzq;

    private String fktj;

    private Integer htlb;

    private Integer sfndkjxy;

    private Integer fklx;

    private String cxsm;

    private BigDecimal htzje;

    private Integer yjsfkykp;

    private Integer htlx2;

    private String fjsc;

    private String xglc;

    private String sysl;

    private Integer sfyddhzhbzrsqlc;

    private String ddhzhbzrsqlch;

    private String hzflxht;

    private String hzhb;

    private String dfdwmc1;

    private Integer htdyfs;

    private String erpSysl;

    private String fplx1;

    private Integer sfdfxgz;

    private Integer fqrsj;

    private Integer fplxoa;

    private Integer sfzdgys;

    private String a;

    private String glcgysqlc;

    private Integer formmodeid;

    private Integer modedatacreater;

    private Integer modedatacreatertype;

    private String modedatacreatedate;

    private String modedatacreatetime;

    private Integer modedatamodifier;

    private String modedatamodifydatetime;

    @TableField("MODEUUID")
    private String modeuuid;

    private String bt;

    private String szbmTxt;

    private String sqrTxt;

    private String szppTxt;

    private String sfxqTxt;

    private String hzflxTxt;

    private String sfzdgysTxt;

    private String glcgysqlcTxt;

    private String wfdwmcTxt;

    private String hzhbTxt;

    private String htlx2Txt;

    private String htlbTxt;

    private String sfndkjxyTxt;

    private String bzTxt;

    private String yjsfkykpTxt;

    private String nfkpTxt;

    private String nsrzzTxt;

    private String fplx1Txt;

    private String erpSyslTxt;

    private String kpqkTxt;

    private String sfdfxgzTxt;

    private String htfxjbTxt;

    private String srmlczt;


    private String hzhbErpnum; //对方单位名称_ERP编码（供应商）

    private String wfdwmcErpnum; //我方单位名称_ERP编码(采购商)


}
