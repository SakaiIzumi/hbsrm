package net.bncloud.event.repository;

import net.bncloud.event.domain.EventDetail;
import net.bncloud.event.domain.EventDetailData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDetailDataRepository extends JpaRepository<EventDetailData, Long> {
}
