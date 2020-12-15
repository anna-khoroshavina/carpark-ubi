package com.example.carpark.exception;

import lombok.Getter;

@Getter
public class OccupiedChargingPointException extends RuntimeException {

    private final String id;

    public OccupiedChargingPointException(String id) {
        super(String.format("Charging Point with id = %s is already occupied", id));
        this.id = id;
    }

}
