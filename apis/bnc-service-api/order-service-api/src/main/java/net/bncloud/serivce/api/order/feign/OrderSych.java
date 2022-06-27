package net.bncloud.serivce.api.order.feign;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class OrderSych implements Serializable {

    private static final long serialVersionUID = -4691933337248607676L;


    private String purchaseCode;
    private String purchaseName;
    private Long orgId;
    private List<String > supplierCode;
    private Map<String,String> supplierCodeNameMap;

}
