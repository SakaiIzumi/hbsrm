package net.bncloud.delivery.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.delivery.entity.FactoryInfo;

import java.io.Serializable;


/**
 * @author liyh
 * @description 工厂主数据请求参数
 * @since 2022/5/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FactoryInfoParam extends FactoryInfo implements Serializable {

}
