package net.bncloud.delivery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * desc: 采购方/供应商 工厂假期管理
 *
 * @author Rao
 * @Date 2022/05/09
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("t_factory_vacation")
public class FactoryVacation extends BaseEntity {
    private static final long serialVersionUID = -6799854636307934203L;

    /**
     * 所属类型 供应商/采购方
     */
    private String belongType;

    /**
     * 所属方 供应商code/采购方code
     */
    private String belongCode;

    /**
     * 所属方 供应商名字/采购方名字
     */
    private String belongName;

    /**
     * 工厂编码
     */
    private String factoryNumber;

    /**
     * 假期类型  节假日/调休休息
     */
    private String vacationType;

    /**
     * 假期来源 自动订阅的节假日  0-手动新增 1-自动订阅 2-非默认工作日的假期
     */
    private String sourceType;

    /**
     * 假期日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private Date vacationDate;

    /**
     * 星期几
     */
    private Integer dayInWeekNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 启用状态 1-启用 0-禁用
     */
    private Integer status;

    /**
     * 工厂名称
     */
    private String factoryName;

    /**
     * 工厂id
     */
    private Long factoryId;

}
