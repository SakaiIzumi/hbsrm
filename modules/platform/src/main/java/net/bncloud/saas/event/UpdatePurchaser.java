package net.bncloud.saas.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class UpdatePurchaser implements Serializable {
    private Long id;
    private String code;
    private String name;
    private String artificialPerson;
    private String description;
    private Boolean enabled;

    public static UpdatePurchaser of(Long id, String code, String name, String artificialPerson, String description, Boolean enabled) {
        return new UpdatePurchaser(id, code, name, artificialPerson, description, enabled);
    }

}
