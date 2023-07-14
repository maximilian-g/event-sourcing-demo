package com.maximilian.es.event;

import com.maximilian.es.aggregate.Customer;
import com.maximilian.es.event.data.CustomerCreateData;
import com.maximilian.es.exception.UnknownAggregateException;
import com.maximilian.es.service.SerializationHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Objects;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(CustomerCreatedEvent.EVENT_TYPE)
public class CustomerCreatedEvent extends BaseEvent {

    public static final String EVENT_TYPE = "customer-create";

    @Override
    public void apply(Object aggregate, SerializationHelper helper) {
        Objects.requireNonNull(aggregate, "Aggregate cannot be null");
        if(!(aggregate instanceof Customer customer)) {
            throw new UnknownAggregateException("Got unknown aggregate. Class " +
                    aggregate.getClass().getName());
        }
        CustomerCreateData customerCreateData = helper.fromJson(getData(), CustomerCreateData.class);
        customer.setName(customerCreateData.getName());
        customer.setBalance(customerCreateData.getBalance());
        customer.setId(getEntityId());
    }

}
