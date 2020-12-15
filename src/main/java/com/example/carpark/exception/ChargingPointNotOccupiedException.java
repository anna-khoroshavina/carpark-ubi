package com.example.carpark.exception;

import lombok.Getter;

@Getter
public class ChargingPointNotOccupiedException extends RuntimeException {

    private final String id;

    public ChargingPointNotOccupiedException(String id) {
        super(String.format("Charging Point with id = %s is not occupied", id));
        this.id = id;
    }

}
