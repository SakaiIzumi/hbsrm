package net.bncloud.file.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "f_storage_config_local")
@Getter
@Setter
public class LocalStorage extends AbstractAuditingEntity {

    private static final long serialVersionUID = -4500074476676327451L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rootDir;
}
