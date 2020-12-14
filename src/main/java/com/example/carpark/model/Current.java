package com.example.carpark.model;

import lombok.Getter;

@Getter
public enum Current {
    OVERALL(100),
    MIN(10),
    MAX(20);

    private final int value;

    Current(int value) {
        this.value = value;
    }
}