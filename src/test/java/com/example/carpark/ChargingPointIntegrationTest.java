package com.example.carpark;

import com.example.carpark.model.ChargingPoint;
import com.example.carpark.model.Current;
import com.example.carpark.repository.ChargingPointRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ChargingPointIntegrationTest {

    private static final String UNPLUG_URL = "/cp-management/charging-points/{id}/unplug";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ChargingPointRepository repository;

    @Before
    public void initDb() {
        repository.saveAll(
                Arrays.asList(
                        new ChargingPoint(UUID.randomUUID(), "CP1", false, Current.MIN.getValue(), OffsetDateTime.now()),
                        new ChargingPoint(UUID.randomUUID(), "CP2", false, Current.MIN.getValue(), OffsetDateTime.now()),
                        new ChargingPoint(UUID.randomUUID(), "CP3", false, Current.MAX.getValue(), OffsetDateTime.now()),
                        new ChargingPoint(UUID.randomUUID(), "CP4", false, Current.MAX.getValue(), OffsetDateTime.now()),
                        new ChargingPoint(UUID.randomUUID(), "CP5", false, Current.MAX.getValue(), OffsetDateTime.now()),
                        new ChargingPoint(UUID.randomUUID(), "CP6", false, Current.MAX.getValue(), OffsetDateTime.now()),
                        new ChargingPoint(UUID.randomUUID(), "CP7", true, 0, OffsetDateTime.now()),
                        new ChargingPoint(UUID.randomUUID(), "CP8", true, 0, OffsetDateTime.now()),
                        new ChargingPoint(UUID.randomUUID(), "CP9", true, 0, OffsetDateTime.now()),
                        new ChargingPoint(UUID.randomUUID(), "CP10", true, 0, OffsetDateTime.now())
                )
        );
    }

    @After
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void whenUnpluggedWithMaxCurrent_thenOtherGetMaxCurrent() throws Exception {
        ChargingPoint cpFromDb =
                repository.findIsNotFree(Current.MAX.getValue(), PageRequest.of(0, 1)).getContent().get(0);

        mockMvc.perform(put(UNPLUG_URL, cpFromDb.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.free", is(true)))
                .andExpect(jsonPath("$.consumedCurrent", is(0)));

        List<ChargingPoint> occupiedCps = repository.findAll().stream()
                .filter(cp -> !cp.isFree()).collect(Collectors.toList());

        assertThat(occupiedCps.size()).isEqualTo(5);
        assertThat(occupiedCps).extracting(ChargingPoint::getConsumedCurrent)
                .allSatisfy(current -> assertThat(current).isEqualTo(Current.MAX.getValue()));

    }

}
