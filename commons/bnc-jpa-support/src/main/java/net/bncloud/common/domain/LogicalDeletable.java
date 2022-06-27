package net.bncloud.common.domain;

public interface LogicalDeletable {

    boolean isDeleted();

    void setDeleted(boolean deleted);
}
