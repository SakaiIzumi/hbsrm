package net.bncloud.event.repository;

import net.bncloud.event.domain.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long>, JpaSpecificationExecutor<EventType> {
    Optional<EventType> findOneByCode(String eventCode);
}
