package net.bncloud.serivce.api.order.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MrpOrderCreatePurchaseOrderBillReturnParamDTO implements Serializable {

    /**
     * 订单的id
     * */
    private Long orderId;

    /**
     * 回写的fid
     * */
    private Long fId;

    /**
     * 订单的fNumber
     * */
    private String fNumber;

    /**
     * 回写的erp状态
     * */
    private String erpStatus;

    /**
     * 回写的erp状态
     * */
    private List<returnDetail> returnDetailList;

    //明细内部类
    @NoArgsConstructor
    @Data
    public static class returnDetail {
        /**
         * 明细的id
         * */
        private Long id;
        /**
         * 回写明细的erpId
         * */
        private Long erpId;

    }





}
