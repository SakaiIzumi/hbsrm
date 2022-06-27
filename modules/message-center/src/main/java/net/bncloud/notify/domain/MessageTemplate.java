package net.bncloud.notify.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_message_template")
@Getter
@Setter
public class MessageTemplate extends AbstractAuditingEntity {

    private static final long serialVersionUID = -8617889915531133499L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String scene;
    @Column(length = 1024)
    private String description;
    private String title;
    @Column(length = 10240)
    private String contentTpl;

}
