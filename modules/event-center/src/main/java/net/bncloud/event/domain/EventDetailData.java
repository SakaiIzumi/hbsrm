package net.bncloud.event.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "evt_event_detail_data")
@Getter
@Setter
public class EventDetailData {

    @Id
    private Long eventId;

    @Lob
    @Column
    private String data;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false)
    private EventDetail eventDetail;
}
