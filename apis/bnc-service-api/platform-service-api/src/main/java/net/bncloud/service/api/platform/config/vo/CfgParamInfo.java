package net.bncloud.service.api.platform.config.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/03/11
 **/
@Data
public class CfgParamInfo implements Serializable {
    private static final long serialVersionUID = 6672451923461622233L;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 组织ID
     */
    private Long orgId;
    /**
     * key
     */
    private String code;
    /**
     * 值
     */
    private String value;
    /**
     * 备注
     */
    private String remark;

    /**
     * 类型
     */
    private String type;

}
