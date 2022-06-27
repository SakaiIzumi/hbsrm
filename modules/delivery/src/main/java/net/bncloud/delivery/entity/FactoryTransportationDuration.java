package net.bncloud.delivery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * desc: 工厂运输时长
 *   类型   1、 厂 --> 厂   2、 供应商 --> 厂  3、厂 --> 采购方  4、供应商 --> 采购方
 *   设计建议：一个类型一张表
 * @author Rao
 * @Date 2022/05/07
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("t_factory_transportation_duration")
@AllArgsConstructor
@NoArgsConstructor
public class FactoryTransportationDuration extends BaseEntity {
    private static final long serialVersionUID = 4727819322007912484L;


    /**
     * 发货厂id
     */
    @NotNull(message = "发货厂未指定！")
    private Long deliveryFactoryId;

    /**
     * 收货厂id
     */
    @NotNull(message = "收货厂未指定！")
    private Long receiptFactoryId;

    /**
     * 运输方式
     */
    @NotNull(message = "运输方式未指定！")
    @Length(min = 1,message = "运输方式未指定！")
    private String transportWay;

    /**
     * 运输时长（/天）  用字符串是为了兼顾后续 0.5天的情况
     */
    @NotNull(message = "运输时长未填写！")
    @Length(min = 1,message = "运输时长未填写！")
    private String transportDuration;

    /**
     * 备注
     */
    private String remark;

}
