package net.bncloud.saas.tenant.service.dto;

public class ManagerOperatePermission {
    private boolean transferable;
    private boolean deletable;

    public boolean isTransferable() {
        return transferable;
    }

    public ManagerOperatePermission setTransferable(boolean transferable) {
        this.transferable = transferable;
        return this;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public ManagerOperatePermission setDeletable(boolean deletable) {
        this.deletable = deletable;
        return this;
    }
}
