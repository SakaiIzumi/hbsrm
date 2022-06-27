package net.bncloud.file.domain;

import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
//@Table(name = "f_storage_config_ftp")
public class FtpStorage extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1788656318717365975L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
