package net.bncloud.event.domain;

import lombok.*;
import lombok.experimental.Accessors;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.event.domain.vo.MessageTpl;
import net.bncloud.event.domain.vo.RoleVO;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "evt_event_type")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventType extends AbstractAuditingEntity {
    private static final long serialVersionUID = 3917437660881823975L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String module;
    private String scene;
    private String description;
    private boolean disabled;
    private String modularType;
    private String receiverType;
    private Integer bisType;


    /**
     * 是否发送消息
     */
    private boolean notify;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "tag", column = @Column(name = "msg_tpl_tag")),
            @AttributeOverride(name = "name", column = @Column(name = "msg_tpl_name"))
    })
    private MessageTpl tpl;

    @ElementCollection(targetClass = RoleVO.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "evt_event_role", joinColumns = {@JoinColumn(name = "evt_type_id", referencedColumnName = "id")})
    private List<RoleVO> roles;

    @OneToMany(mappedBy = "eventType", fetch = FetchType.LAZY ,cascade = {CascadeType.REMOVE})
    private List<EventDetail> eventDetails;
}
