package net.bncloud.order.vo.event;


import lombok.Data;
import net.bncloud.common.util.DateUtil;
import net.bncloud.order.entity.OrderOperationLog;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单操作记录表
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
@Data
public class OrderOperationLogEvent extends OrderOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String addTime = DateUtil.formatDateTime(new Date());
    private Long businessId;




}
