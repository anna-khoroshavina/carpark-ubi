package com.example.carpark.repository;

import com.example.carpark.model.ChargingPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChargingPointRepository extends JpaRepository<ChargingPoint, UUID> {

    @Query("select cp from ChargingPoint cp where isFree = false and consumedCurrent = ?1")
    Page<ChargingPoint> findIsNotFree(int current, Pageable pageable);

    @Query("SELECT SUM(cp.consumedCurrent) FROM ChargingPoint cp")
    int countOverallCurrent();

}
