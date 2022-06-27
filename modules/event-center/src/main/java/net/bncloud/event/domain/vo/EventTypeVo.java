package net.bncloud.event.domain.vo;

import lombok.*;
import lombok.experimental.Accessors;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.event.domain.EventDetail;
import net.bncloud.event.domain.EventType;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
public class EventTypeVo extends EventType {


    private List<Long> rolesList;




}
