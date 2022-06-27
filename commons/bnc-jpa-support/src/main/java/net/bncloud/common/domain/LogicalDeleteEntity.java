package net.bncloud.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class LogicalDeleteEntity implements Serializable, LogicalDeletable {

    private static final long serialVersionUID = 1445405905602777961L;
    @JsonIgnore
    @Column(name = "is_deleted", nullable = false)
    protected boolean deleted;

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
