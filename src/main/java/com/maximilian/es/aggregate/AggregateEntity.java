package com.maximilian.es.aggregate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@javax.persistence.Entity
@Table(name = "aggregate_entity")
public class AggregateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long entityId;
    @Column(nullable = false)
    private long entityType;
    @Column(nullable = false)
    @Version
    private long revision;

}
