package net.bncloud.delivery.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchDeleteWorkDayParam implements Serializable {
    List<FactoryWorkdayParam> factoryWorkdayParamList;


    private String belongType; //分了两个接口,这个字段可以不用了
}
