package net.bncloud.order.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/03/14
 **/
@Data
public class CfgParam implements Serializable {

    private Long id;
    private Long orgId;
    private String code;
    private String value;
    private String remark;

}

