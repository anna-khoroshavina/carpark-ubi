package com.example.carpark.service;

import com.example.carpark.model.ChargingPoint;

public interface ChargingPointService {

    Iterable<ChargingPoint> findAll();

    ChargingPoint plug(String id);

    ChargingPoint unplug(String id);
}
