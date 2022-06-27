package net.bncloud.delivery.vo;


import net.bncloud.delivery.entity.DeliveryHarbor;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * <p>
 * 港口信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Data
public class DeliveryHarborVo extends DeliveryHarbor implements Serializable {

    private static final long serialVersionUID = 1L;



}
