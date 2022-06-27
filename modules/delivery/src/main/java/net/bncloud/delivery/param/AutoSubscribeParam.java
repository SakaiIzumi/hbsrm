package net.bncloud.delivery.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class AutoSubscribeParam implements Serializable {
    /**
     * 开关状态
     * */
    private String changer;

    /**
     * 采购/供应商编码
     * */
    private String code;

    /**
     * 采购/供应商类型
     * */
    private String belongType;


}
