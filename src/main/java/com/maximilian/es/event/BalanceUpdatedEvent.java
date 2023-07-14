package com.maximilian.es.event;

import com.maximilian.es.aggregate.Customer;
import com.maximilian.es.command.BalanceChangeType;
import com.maximilian.es.event.data.BalanceChangeData;
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
@DiscriminatorValue(BalanceUpdatedEvent.EVENT_TYPE)
public class BalanceUpdatedEvent extends BaseEvent {

    public static final String EVENT_TYPE = "balance-update";

    @Override
    public void apply(Object aggregate, SerializationHelper helper) {
        Objects.requireNonNull(aggregate, "Aggregate cannot be null");
        if(!(aggregate instanceof Customer customer)) {
            throw new UnknownAggregateException("Got unknown aggregate. Class " +
                    aggregate.getClass().getName());
        }
        BalanceChangeData data = helper.fromJson(getData(), BalanceChangeData.class);
        customer.setBalance(customer.getBalance() +
                (data.getType() == BalanceChangeType.WITHDRAW ? data.getAmount() * -1 : data.getAmount()));
    }

}
