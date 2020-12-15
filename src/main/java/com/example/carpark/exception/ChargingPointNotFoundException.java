package com.example.carpark.exception;

import lombok.Getter;

@Getter
public class ChargingPointNotFoundException extends RuntimeException {

    public ChargingPointNotFoundException() {
        super("Charging Point with is not found");
    }

}
