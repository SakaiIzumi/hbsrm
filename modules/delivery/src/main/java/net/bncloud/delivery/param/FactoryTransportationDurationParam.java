package net.bncloud.delivery.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.bncloud.delivery.entity.FactoryTransportationDuration;

import java.io.Serializable;

/**
 * @author liyh
 * @description 工厂运输时长请求参数
 * @since 2022/5/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactoryTransportationDurationParam extends FactoryTransportationDuration implements Serializable {

    /**
     * 供应商编码
     */
    private String supplierCode;

}
