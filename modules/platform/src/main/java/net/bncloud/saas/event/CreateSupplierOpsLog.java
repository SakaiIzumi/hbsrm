package net.bncloud.saas.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class CreateSupplierOpsLog implements Serializable {
    private static final long serialVersionUID = -6272417361344077702L;
    private Long supplierId;
    private String content;
    private String remark;
    private String opsUserName;

    public static CreateSupplierOpsLog of(Long supplierId, String content, String remark, String opsUserName) {
        return new CreateSupplierOpsLog(supplierId, content, remark, opsUserName);
    }

}
