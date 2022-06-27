package net.bncloud.order.param;


import net.bncloud.order.entity.OrderOperationLog;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;


/**
 * <p>
 * 订单操作记录表
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
@Data
public class OrderOperationLogParam extends OrderOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;



}
