package com.maximilian.es.event;

import com.maximilian.es.service.SerializationHelper;

public interface Event {

    void apply(Object aggregate, SerializationHelper helper);

}
