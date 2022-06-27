package net.bncloud.event.repository;

import net.bncloud.event.domain.EventDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDetailRepository extends JpaRepository<EventDetail, Long> {
}
