package net.bncloud.delivery.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayAndSubscribeConfigParam implements Serializable {
    /**
     * 默认工作日的配置
     * */
    private Map<String, Integer> defaultWorkdayConfigMap=new HashMap<>();

    /**
     * 自动订阅法定的配置
     * */
    private Boolean jsonObjectForAuto;

    /**
     * 工作日业务方法中使用  自动订阅法定节假日的list
     * */
    private List<LocalDate> localDateListForAuto=new ArrayList<>();

    /**
     * 工作日业务方法中使用  自动订阅法定节假日需要上班的list
     * */
    private List<LocalDate> localDate996ListForAuto=new ArrayList<>();

}
