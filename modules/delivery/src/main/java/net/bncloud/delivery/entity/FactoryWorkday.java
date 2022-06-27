package net.bncloud.delivery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.base.BaseEntity;

/**
 * desc: 工作日信息维护
 * 1、与协同配置的默认工作日要联系起来。
 *
 * @author Rao
 * @Date 2022/05/09
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_factory_workday")
public class FactoryWorkday extends BaseEntity {
    private static final long serialVersionUID = 8767769139219766741L;

    /**
     * 所属类型 供应商/采购方
     */
    private String belongType;

    /**
     * 所属方 供应商code/采购方code
     */
    private String belongCode;

    /**
     * 所属名称 供应商名称
     */
    private String belongName;

    /**
     * 工厂Code
     */
    private String factoryNumber;

    /**
     * 工作日起始星期  1-7
     */
    private Integer weekStartNum;

    /**
     * 工作日周结束数
     */
    private Integer weekEndNum;

    /**
     * 工厂名字
     */
    private String factoryName;

    /**
     * 工厂id
     */
    private Long factoryId;

}
