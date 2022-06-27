package net.bncloud.service.api.platform.sys.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class SetSupplierConfigDTO implements Serializable {
    /**
     * mrp配置在配置表中的编码
     * */
    private String code;

    /**
     * 供应商编码(如果采购有多个,这个参数也可以作为采购编码 )
     * */
    private String supplierCode;

    private Boolean onOff;


}
