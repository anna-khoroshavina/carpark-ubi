package com.example.carpark.util;

import com.example.carpark.dto.ChargingPointDto;
import com.example.carpark.model.ChargingPoint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ModelMapper {

    public ChargingPointDto map(ChargingPoint cp) {
        return new ChargingPointDto(
                cp.getId().toString(), cp.getName(), cp.isFree(),
                cp.getConsumedCurrent(), cp.getConnectedDateTime()
        );
    }

    public List<ChargingPointDto> toDtoList(Iterable<ChargingPoint> chargingPoints) {
        return StreamSupport.stream(chargingPoints.spliterator(), false)
                .map(this::map)
                .collect(Collectors.toList());
    }

}
