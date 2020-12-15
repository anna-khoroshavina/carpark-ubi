package com.example.carpark.exception;

import lombok.Getter;

@Getter
public class InvalidIdException extends RuntimeException {

    private final String id;

    public InvalidIdException(String id) {
        super(String.format("Provided Id = %s is not valid", id));
        this.id = id;
    }

}
