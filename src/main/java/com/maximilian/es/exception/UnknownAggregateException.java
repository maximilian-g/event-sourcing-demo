package com.maximilian.es.exception;

public class UnknownAggregateException extends GeneralException {

    public UnknownAggregateException() {
    }

    public UnknownAggregateException(String message) {
        super(message);
    }

    public UnknownAggregateException(String message, Throwable cause) {
        super(message, cause);
    }

}
