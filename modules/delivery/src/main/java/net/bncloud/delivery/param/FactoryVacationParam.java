package net.bncloud.delivery.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.delivery.entity.FactoryVacation;

import java.io.Serializable;

/**
 * desc: 采购方/供应商 工厂假期管理param
 *
 * @author liyh
 * @Date 2022/05/16
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class FactoryVacationParam extends FactoryVacation implements Serializable {

    /**
     *节假日期开始时间
     **/
    private String startDate;

    /**
     *节假日期结束时间
     **/
    private String endDate;

    /**
     *判断当前是采购方查看还是供应方查看列表  0-采购  1-供应
     *
     **/
    private String port;

    /**
     *判断当前是访问页面的哪一个页签 0 采购  1供应
     *
     **/
    private String tab;



}
