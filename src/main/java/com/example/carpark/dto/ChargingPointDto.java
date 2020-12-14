package com.example.carpark.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChargingPointDto {

    private String id;
    private String name;
    private boolean isFree;
    private int consumedCurrent;
    @JsonIgnore
    private OffsetDateTime connectedDateTime;

}