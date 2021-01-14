package com.springjpa.study.jpashop.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class NotEnoughStockException extends RuntimeException {

    @JsonIgnore
    private static final long serialVersionUID = 3607065993882703237L;

    public NotEnoughStockException(String message) {
        super(message);
    }
    
    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}