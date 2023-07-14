package com.maximilian.es.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements BaseAggregate {

    public static final String CUSTOMER_AGGREGATE_NAME = "customer";

    private long id;
    private String name;
    private long balance;

    @Override
    public String getAggregateName() {
        return CUSTOMER_AGGREGATE_NAME;
    }

}
