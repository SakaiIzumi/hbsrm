package net.bncloud.saas.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class AllBindPurchaser implements Serializable {
    private Long id;
    private Long orgId;

    public static AllBindPurchaser of(Long id, Long orgId) {
        return new AllBindPurchaser(id, orgId);
    }

}
