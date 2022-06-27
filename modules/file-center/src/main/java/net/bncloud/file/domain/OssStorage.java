package net.bncloud.file.domain;

import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
//@Table(name = "f_storage_config_oss")
public class OssStorage extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
