package com.maximilian.es.repository;

import com.maximilian.es.aggregate.AggregateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;

public interface AggregateEntityRepository extends JpaRepository<AggregateEntity, Long> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    AggregateEntity getAggregateEntityByEntityId(Long entityId);

}
