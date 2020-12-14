package com.example.carpark.service;

import com.example.carpark.model.ChargingPoint;
import com.example.carpark.model.Current;
import com.example.carpark.repository.ChargingPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChargingPointServiceImpl implements ChargingPointService {

    private static final String DEFAULT_SORT_PROPERTY = "connectedDateTime";
    private static final int DEFAULT_START_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 2;

    private final ChargingPointRepository chargingPointRepository;

    @Override
    public Iterable<ChargingPoint> findAll() {
        return chargingPointRepository.findAll();
    }

    @Override
    public ChargingPoint plug(String id) {
        ChargingPoint chargingPoint = chargingPointRepository.findById(toUUID(id)).orElse(null);

        validateBeforePlug(chargingPoint);
        chargingPoint.setFree(false);
        chargingPoint.setConnectedDateTime(OffsetDateTime.now());

        PageRequest pageRequest = PageRequest.of(
                DEFAULT_START_PAGE, DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Order.asc(DEFAULT_SORT_PROPERTY))
        );
        List<ChargingPoint> occupiedCps =
                chargingPointRepository.findIsNotFree(Current.MAX.getValue(), pageRequest).getContent();

        if (isFullCapacityReached()) {
            occupiedCps.forEach(oldCp -> oldCp.setConsumedCurrent(Current.MIN.getValue()));
            int currentValue = occupiedCps.size() == 1 ? Current.MIN.getValue() : Current.MAX.getValue();
            chargingPoint.setConsumedCurrent(currentValue);
        } else {
            chargingPoint.setConsumedCurrent(Current.MAX.getValue());
        }

        ChargingPoint savedCp = chargingPointRepository.save(chargingPoint);
        chargingPointRepository.saveAll(occupiedCps);

        return savedCp;
    }

    private void validateBeforePlug(ChargingPoint chargingPoint) {
        if (chargingPoint == null) {
            throw new IllegalArgumentException("Charging Point is not found");
        } else if (!chargingPoint.isFree()) {
            throw new IllegalStateException("Charging Point is already occupied");
        }
    }

    private void validateBeforeUnplug(ChargingPoint chargingPoint) {
        if (chargingPoint == null) {
            throw new IllegalArgumentException("Charging Point is not found");
        } else if (chargingPoint.isFree()) {
            throw new IllegalStateException("Charging Point is not occupied");
        }
    }

    @Override
    public ChargingPoint unplug(String id) {
        ChargingPoint chargingPoint = chargingPointRepository.findById(toUUID(id)).orElse(null);

        validateBeforeUnplug(chargingPoint);

        int pageSize = chargingPoint.getConsumedCurrent() == Current.MAX.getValue() ? 2 : 1;
        PageRequest pageRequest = PageRequest.of(
                DEFAULT_START_PAGE, pageSize,
                Sort.by(Sort.Order.desc(DEFAULT_SORT_PROPERTY))
        );
        List<ChargingPoint> occupiedCps =
                chargingPointRepository.findIsNotFree(Current.MIN.getValue(), pageRequest).getContent();
        occupiedCps.forEach(p -> p.setConsumedCurrent(Current.MAX.getValue()));

        chargingPoint.setFree(true);
        chargingPoint.setConsumedCurrent(0);

        ChargingPoint savedCp = chargingPointRepository.save(chargingPoint);
        chargingPointRepository.saveAll(occupiedCps);

        return savedCp;
    }

    private UUID toUUID(String id) {
        return UUID.fromString(id);
    }

    private boolean isFullCapacityReached() {
        return chargingPointRepository.countOverallCurrent() == Current.OVERALL.getValue();
    }

}
