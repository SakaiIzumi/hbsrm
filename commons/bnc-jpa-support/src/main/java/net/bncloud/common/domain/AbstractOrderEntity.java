
package net.bncloud.common.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractOrderEntity extends AbstractAuditingEntity implements Ordered {

    private static final long serialVersionUID = -3834706045102943542L;
    @Column(name = "order_num")
    protected Integer order;

    @Override
    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public Integer getOrder() {
        return this.order;
    }
}
