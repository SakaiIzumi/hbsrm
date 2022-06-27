package net.bncloud.saas.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class CreatePurchaser implements Serializable {
    private String code;
    private String name;
    private String artificialPerson;
    private String description;
    private Boolean enabled;
    private Long orgId;


    public static CreatePurchaser of(String code, String name, String artificialPerson, String description, Boolean enabled, Long orgId) {
        return new CreatePurchaser(code, name, artificialPerson, description, enabled, orgId);
    }

}
