package net.bncloud.saas.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class BindPurchaser implements Serializable {
    private Long id;
    private Long orgId;
    private List<Long> supplierIds;

    public static BindPurchaser of(Long id, Long orgId, List<Long> supplierIds) {
        return new BindPurchaser(id, orgId, supplierIds);
    }

}
