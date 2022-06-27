package net.bncloud.delivery.vo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * desc: 需求日期数据传输对象
 *    其实应该叫上下文
 *
 * @author Rao
 * @Date 2022/06/11
 **/
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class DemandDateDto implements Serializable {

    private static final long serialVersionUID = 3107434550090752500L;

    /**
     * 运算需求日期
     */
    private LocalDate demandLocalDate;

    /**
     * 建议发货日期
     */
    private LocalDate proposalDeliveryDate;

    /**
     * 预计到货日期
     */
    private LocalDate estimateReceiveDate;


    public LocalDateTime estimateReceiveDateToLocalDateTime(){
        return estimateReceiveDate.atStartOfDay();
    }

}
