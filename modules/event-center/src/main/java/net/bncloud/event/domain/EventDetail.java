package net.bncloud.event.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.event.domain.vo.FromUser;

import javax.persistence.*;

@Entity
@Table(name = "evt_event_detail")
@Getter
@Setter
public class EventDetail extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1707199357830427078L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "from_user_id")),
            @AttributeOverride(name = "name", column = @Column(name = "from_user_name"))
    })
    private FromUser user;

    @JsonIgnoreProperties("eventType")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false, updatable = false)
    private EventType eventType;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "eventDetail", cascade = {CascadeType.REMOVE, CascadeType.PERSIST,
            CascadeType.MERGE})
    private EventDetailData detailData;


    @Transient
    private Long orgId;

    @Transient
    private String supplierCode;

    private String smsParams;

    private String smsTempCode;

    private Integer smsMsgType;
}
