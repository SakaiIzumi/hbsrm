package net.bncloud.delivery.vo;

import lombok.Data;
import net.bncloud.delivery.entity.FactoryVacation;

import java.io.Serializable;
import java.util.Map;

/**
 * desc: 采购方/供应商 工厂假期管理vo
 *
 * @author liyh
 * @Date 2022/05/16
 **/
@Data
public class FactoryVacationVo extends FactoryVacation implements Serializable {

    /**
     * 可操作按钮
     */
    private Map<String,Boolean> permissionButton;

    /**
     * 按钮开关true/false (前端使用)
     */
    private Boolean button;

    public void adjustButton(Integer onOff){
        this.button=onOff.equals(1)?true:false;
    }

}
