package net.bncloud.delivery.param;

import lombok.Data;
import net.bncloud.delivery.entity.FactoryWorkday;

import java.io.Serializable;
import java.util.List;

/**
 * desc: 工作日信息维护
 *
 * @author liyh
 * @Date 2022/05/16
 **/
@Data
public class FactoryWorkdayParam extends FactoryWorkday implements Serializable {
    /**
     *所属方 供应商编码
     **/
//    private String code;

    /**
     *所属方 名称
     **/
//    private String name;

}
