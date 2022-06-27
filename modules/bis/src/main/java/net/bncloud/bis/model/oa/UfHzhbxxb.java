package net.bncloud.bis.model.oa;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 合作伙伴主表
 * </p>
 *
 * @author jabngit frame
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UfHzhbxxb对象", description="合作伙伴主表")
public class UfHzhbxxb implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField("requestId")
    private Integer requestId;

    private String tjrq;

    private String hzfbm;

    /**
     * 0 供应商 1 客户
     */
    private Integer hzxz;

    private String qyqc;

    private String qyjc;

    private String zrrxm;

    private String zrrjc;

    private Integer xb;

    private String mslxrdh;

    private String clrq;

    private String tyshxydm;

    private String qyyyzz;

    private String scjyxkz;

    private String gszcdz;

    private String qyjj;

    private String sfzhz;

    private String sjdz;

    private String hzywms;

    private Integer formmodeid;

    private Integer modedatacreater;

    private Integer modedatacreatertype;

    private String modedatacreatedate;

    private String modedatacreatetime;

    private Integer modedatamodifier;

    private String modedatamodifydatetime;

    @TableField("MODEUUID")
    private String modeuuid;

    private String qy;

    private String zrr;

    private String mslxrdh1;

    private String clrq1;

    private String hzywms1;

    /**
     * 0 企业 1 自然人
     */
    private Integer hzhblx;

    private String sjdz1;

    private String zylxr;

    private Integer sfhz;

    private String qyyc2;

    private String qyyc3;

    private String qyyc4;

    private String qyyc5;

    private String qyyc6;

    private String qyyc7;

    private String qyyc8;

    private String zrryc2;

    private String zrryc3;

    private String zrryc4;

    private String zrryc5;

    private String zrryc7;

    private String zrryc8;

    private String zrryc6;

    private Integer gyslb;

    /**
     * 0 是 1 否
     */
    private Integer sfhzdgys;

    /**
     * 金蝶编码
     */
    private String jdnumber;

    private String jdid;

    private String qyzczj;

    private String qd;

    private String khbm;

    private String ck;

    private String gh;

    private Integer mslxr;

    private Integer mslxr1;

    private String gh1;

    private String khfzbm;

    private String khfz;

    private String xsy;

    private String xsy1;

    private String jdcg;

    private String lssjfjdz;

    private String hzhbxxjdczzy;

    private String wfgsmc;

}
