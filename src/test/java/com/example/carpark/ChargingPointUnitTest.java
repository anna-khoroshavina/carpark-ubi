package com.example.carpark;

import com.example.carpark.model.ChargingPoint;
import com.example.carpark.model.Current;
import com.example.carpark.repository.ChargingPointRepository;
import com.example.carpark.service.ChargingPointServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;

import java.time.OffsetDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChargingPointUnitTest {

    @InjectMocks
    private ChargingPointServiceImpl service;
    @Mock
    private ChargingPointRepository repository;

    private ChargingPoint newCp;

    @Before
    public void setUp() throws Exception {
        newCp = new ChargingPoint(UUID.randomUUID(), "CP10", true, 0, OffsetDateTime.now());
        when(repository.findById(any())).thenReturn(Optional.of(newCp));
        when(repository.save(any(ChargingPoint.class))).then(returnsFirstArg());
    }

    @Test
    public void whenLastIsPlugged_thenGetMinCurrent() {
        List<ChargingPoint> occupiedCpsWithMaxCurrent = Collections.singletonList(
                new ChargingPoint(UUID.randomUUID(), "CP9", false, Current.MAX.getValue(), OffsetDateTime.now())
        );

        when(repository.findIsNotFree(anyInt(), any())).thenReturn(new PageImpl<>(occupiedCpsWithMaxCurrent));
        when(repository.countOverallCurrent()).thenReturn(Current.OVERALL.getValue());
        ChargingPoint savedCp = service.plug(newCp.getId().toString());

        assertThat(savedCp.getConsumedCurrent()).isEqualTo(Current.MIN.getValue());
        assertThat(occupiedCpsWithMaxCurrent).extracting(ChargingPoint::getConsumedCurrent)
                .allSatisfy(current -> assertThat(current).isEqualTo(Current.MIN.getValue()));
    }

    @Test
    public void whenPluggedAfterFifth_thenOldestGetMinCurrent() {
        List<ChargingPoint> occupiedCpsWithMaxCurrent = Arrays.asList(
                new ChargingPoint(UUID.randomUUID(), "CP1", false, Current.MAX.getValue(), OffsetDateTime.now()),
                new ChargingPoint(UUID.randomUUID(), "CP2", false, Current.MAX.getValue(), OffsetDateTime.now())
        );

        when(repository.findIsNotFree(anyInt(), any())).thenReturn(new PageImpl<>(occupiedCpsWithMaxCurrent));
        when(repository.countOverallCurrent()).thenReturn(Current.OVERALL.getValue());
        ChargingPoint savedCp = service.plug(newCp.getId().toString());

        assertThat(savedCp.getConsumedCurrent()).isEqualTo(Current.MAX.getValue());
        assertThat(occupiedCpsWithMaxCurrent).extracting(ChargingPoint::getConsumedCurrent)
                .allSatisfy(current -> assertThat(current).isEqualTo(Current.MIN.getValue()));
    }

}
