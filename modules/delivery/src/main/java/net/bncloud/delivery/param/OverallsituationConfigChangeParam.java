package net.bncloud.delivery.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverallsituationConfigChangeParam implements Serializable {
    private String weekStartNum;
    private String weekEndNum;
    /**
     * 采购方编码
     * */
    private String code;
}
