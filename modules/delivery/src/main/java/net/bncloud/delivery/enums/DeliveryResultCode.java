package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.common.api.IResultCode;

/**
 * @ClassName DeliveryResultCode
 * @Description: 收发货返回结果枚举
 * @Author Administrator
 * @Date 2021/4/9
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum DeliveryResultCode implements IResultCode {

    //400(两位业务模块，3位自定义编码)
    PARAM_ERROR(40001001, "参数错误"),
    //400(两位业务模块，3位自定义编码)
    RESOURCE_NOT_FOUND(40001002, "未查询到送货通知"),
    //400(两位业务模块，3位自定义编码)
    ORDER_SERVICE_EXCEPTION(40001003, "订单服务异常"),
    ORDER_SERVICE_REDUCE_FAILED(40001004, "调用订单服务，扣减库存失败"),
    ORDER_SERVICE_RELEASE_FAILED(40001005, "调用订单服务，释放库存失败"),
    ;
    /**
     * code编码
     */
    final int code;
    /**
     * 中文信息描述
     */
    final String message;


}
