package net.bncloud.service.api.platform.user.dto;

import java.io.Serializable;

public class AccountStatus implements Serializable {
    private static final long serialVersionUID = -1797316060116624984L;
    private boolean disabled;
    private boolean accountExpired;
    private boolean accountLocked;

    public AccountStatus() {
    }

    private AccountStatus(boolean disabled, boolean accountExpired, boolean accountLocked) {
        this.disabled = disabled;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
    }

    public static AccountStatus of(boolean disabled, boolean accountExpired, boolean accountLocked) {
        return new AccountStatus(disabled, accountExpired, accountLocked);
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    @Override
    public String toString() {
        return "AccountStatus{" +
                "disabled=" + disabled +
                ", accountExpired=" + accountExpired +
                ", accountLocked=" + accountLocked +
                '}';
    }
}
