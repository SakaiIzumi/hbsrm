package net.bncloud.delivery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("t_factory_subscribe")
public class FactorySubscribe extends BaseEntity {
    private static final long serialVersionUID = -8199703327487742318L;

    /**
     * 工厂id
     * */
    private Long factoryId;

    /**
     * 绑定的年份
     * */
    private String year;

    /**
     * 属于的采购方/供应方编码
     * */
    private String BelongCode;






}
