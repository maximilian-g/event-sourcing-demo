package com.maximilian.es.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "EVENTS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("base")
@DiscriminatorColumn(name = "eventType")
@ToString
@EqualsAndHashCode(of = {"eventId", "entityId"})
public abstract class BaseEvent implements Event {

    @Id
    @Type(type = "uuid-char")
    private UUID eventId = UUID.randomUUID();
    @Column(insertable = false, updatable = false)
    private String eventType;
    @Column(nullable = false)
    private long entityType;
    @Column(nullable = false)
    private long entityId;
    @Column(nullable = false)
    private ZonedDateTime creationDate = ZonedDateTime.now();
    private String data;

}
