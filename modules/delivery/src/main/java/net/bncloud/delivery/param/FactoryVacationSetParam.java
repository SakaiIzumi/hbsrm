package net.bncloud.delivery.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author Rao
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactoryVacationSetParam implements Serializable {
    private static final long serialVersionUID = -2938602481626041348L;
    /**
     * 开始时间
     */
    @NotNull(message = "开始时间未选择！")
    @Length(message = "开始时间未选择！")
    private String startDate;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间未选择！")
    @Length(message = "结束时间未选择！")
    private String endDate;

    /**
     * 假期类型  节假日-0/调休休息-1   原来字段名叫vacationType 适应前端,改成sourceType (现在不用这个了)
     * */
    //@NotNull(message = "假期类型未选择！")
    private String sourceType;

    /**
     * 假期类型  节假日-0/调休休息-1   上面的字段名 跟前端沟通,为了字段统一使用这个
     * */
    //@NotNull(message = "假期类型未选择！")
    private String vacationType;

    /**
     * belongType 供应方/采购方
     * */
    private String belongType;

    /**
     * belongCode 采购方/供应方编码
     * */
    private String belongCode;

    /**
     * 假期日期  更新的时候需要
     * */
    private String vacationDate;

    /**
     * 假期日期的id  更新的时候需要 用于删除
     * */
    private Long id;

    /**
     * 工厂信息  主要是工厂的id 和name
     * */
    private List<FactoryInfoParam> factoryInfoParamList;




}
