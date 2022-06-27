package net.bncloud.file.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.file.domain.vo.User;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "f_file_download")
@Getter
@Setter
public class Download extends AbstractAuditingEntity {

    private static final long serialVersionUID = 5323748357891701777L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "userId", column = @Column(name = "download_by_user_id")),
            @AttributeOverride(name = "userName", column = @Column(name = "download_by_user_name"))
    })
    private User downloadBy;
    @OneToOne
    private FileInfo fileInfo;
}
