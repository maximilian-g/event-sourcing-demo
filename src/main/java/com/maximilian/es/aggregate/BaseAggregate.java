package com.maximilian.es.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface BaseAggregate {

    @JsonIgnore
    String getAggregateName();

}
