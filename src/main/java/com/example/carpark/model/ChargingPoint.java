package com.example.carpark.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChargingPoint {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private boolean isFree;
    private int consumedCurrent;
    private OffsetDateTime connectedDateTime;

}