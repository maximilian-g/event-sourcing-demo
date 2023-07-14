package com.maximilian.es.repository;

import com.maximilian.es.event.BaseEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<BaseEvent, UUID> {

    List<BaseEvent> getBaseEventsByEntityIdOrderByCreationDate(Long entityId);

}
