package net.bncloud.service.api.platform.config.vo.configvo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * desc: 送货协同  送货协同方式
 *
 * @author Rao
 * @Date 2022/06/14
 **/
@NoArgsConstructor
@Data
public class DeliveryCollaborationMethod implements Serializable {
    private static final long serialVersionUID = 6926322882388925336L;

    /**
     * deliveryPlan  true or false
     */
    private String deliveryPlan;
    /**
     * planScheduling  true or false
     */
    private String planScheduling;


    public boolean deliveryPlanValue(){
        try {
            return Boolean.parseBoolean(deliveryPlan);
        }catch (Exception ignore){}
        return false;
    }


    public boolean planSchedulingValue(){
        try {
            return Boolean.parseBoolean(planScheduling);
        }catch (Exception ignore){}
        return false;
    }

}
