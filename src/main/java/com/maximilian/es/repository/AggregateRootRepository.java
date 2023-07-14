package com.maximilian.es.repository;

import com.maximilian.es.aggregate.AggregateRoot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AggregateRootRepository extends JpaRepository<AggregateRoot, Long> {

    Optional<AggregateRoot> findAggregateRootByName(String name);

}
