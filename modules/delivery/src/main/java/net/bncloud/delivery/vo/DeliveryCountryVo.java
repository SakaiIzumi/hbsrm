package net.bncloud.delivery.vo;


import net.bncloud.delivery.entity.DeliveryCountry;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * <p>
 * 国家信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Data
public class DeliveryCountryVo extends DeliveryCountry implements Serializable {

    private static final long serialVersionUID = 1L;



}
