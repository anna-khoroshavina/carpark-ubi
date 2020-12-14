package com.example.carpark.controller;

import com.example.carpark.dto.ChargingPointDto;
import com.example.carpark.model.ChargingPoint;
import com.example.carpark.service.ChargingPointService;
import com.example.carpark.util.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cp-management")
public class ChargingPointController {

    private final ChargingPointService chargingPointService;
    private final ModelMapper mapper;

    @PutMapping("/charging-points/{id}/plug")
    ResponseEntity<ChargingPoint> plug(@PathVariable final String id) {
        return ResponseEntity.ok(chargingPointService.plug(id));
    }

    @PutMapping("/charging-points/{id}/unplug")
    ResponseEntity<ChargingPoint> unplug(@PathVariable final String id) {
        return ResponseEntity.ok(chargingPointService.unplug(id));
    }

    @GetMapping("/charging-points/report")
    ResponseEntity<List<ChargingPointDto>> report() {
        return ResponseEntity.ok(mapper.toDtoList(chargingPointService.findAll()));
    }

}
