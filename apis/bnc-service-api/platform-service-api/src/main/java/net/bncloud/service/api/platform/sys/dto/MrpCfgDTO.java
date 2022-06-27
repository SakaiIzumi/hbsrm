package net.bncloud.service.api.platform.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MrpCfgDTO implements Serializable {
    /**
     * 自动订阅节假日的配置
     * */
    private String autoScribe;
    /**
     * 采购的全局默认工作日的配置
     * */
    private String defaultWorkday;
}
