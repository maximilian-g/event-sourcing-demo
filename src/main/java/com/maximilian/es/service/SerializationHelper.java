package com.maximilian.es.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximilian.es.exception.GeneralException;
import org.springframework.stereotype.Service;

@Service
public class SerializationHelper {

    private final ObjectMapper mapper;

    public SerializationHelper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> T fromJson(String data, Class<T> clazz) {
        try {
            return mapper.readValue(data, clazz);
        } catch (JsonProcessingException e) {
            throw new GeneralException("Could not deserialize object.", e);
        }
    }

    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new GeneralException("Could not serialize object.", e);
        }
    }

}
