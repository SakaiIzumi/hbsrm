package net.bncloud.delivery.param;


import net.bncloud.delivery.entity.DeliveryCustomsInformation;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;


/**
 * <p>
 * 报关资料信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Data
public class DeliveryCustomsInformationParam extends DeliveryCustomsInformation implements Serializable {

    private static final long serialVersionUID = 1L;



}
