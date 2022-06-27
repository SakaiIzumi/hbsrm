package net.bncloud.order.param;


import net.bncloud.order.entity.OrderChangeLog;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;


/**
 * <p>
 * 修改日志表
 * </p>
 *
 * @author 吕享1义
 * @since 2021-03-12
 */
@Data
public class OrderChangeLogParam extends OrderChangeLog implements Serializable {

    private static final long serialVersionUID = 1L;



}
