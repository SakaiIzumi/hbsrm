package net.bncloud.saas.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountStatus {

    private boolean disabled;
    private String disableReason;
    private boolean accountExpired;
    private String expireReason;
    private boolean accountLocked;
    private String lockReason;

    public static AccountStatus defaultStatus() {
        return new AccountStatus(false, null, false, null, false, null);
    }

    public static AccountStatus inactive() {
        return new AccountStatus(true, "未激活", false, null, false, null);
    }
}
