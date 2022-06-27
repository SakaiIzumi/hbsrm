package net.bncloud.delivery.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/03/11
 **/
@Accessors(chain = true)
@Data
public class SendProportionRationVo implements Serializable {

    /**
     * 开启超出
     */
    private boolean openStatus;

    /**
     * 超出百分比
     */
    private BigDecimal exceededPercentage;

    private static final long serialVersionUID = -1742320998302998574L;
}
