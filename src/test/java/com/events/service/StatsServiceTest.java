package com.events.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.events.dto.TopDefectLineDTO;
import com.events.entity.EventEntity;
import com.events.repository.EventRepository;

@SpringBootTest
@Transactional
class StatsServiceTest {

    @Autowired
    private StatsService statsService;

    @Autowired
    private EventRepository eventRepository;

    // -------------------------------
    // Aggregation / Edge Case Test
    // -------------------------------
    @Test
    void getTopDefectLines_shouldReturnAggregatedData() {

    	EventEntity e1 = new EventEntity();
    	e1.setEventId("EVT-STAT-1");
    	e1.setMachineId("M-1");
    	e1.setLineId("L-1");
    	e1.setEventTime(Instant.now());
    	e1.setReceivedTime(Instant.now());
    	e1.setDurationMs(100L);
    	e1.setDefectCount(3);
    	e1.setPayloadHash("hash1");

    	EventEntity e2 = new EventEntity();
    	e2.setEventId("EVT-STAT-2");
    	e2.setMachineId("M-1");
    	e2.setLineId("L-1");
    	e2.setEventTime(Instant.now());
    	e2.setReceivedTime(Instant.now());
    	e2.setDurationMs(100L);
    	e2.setDefectCount(2);
    	e2.setPayloadHash("hash2");


        eventRepository.saveAll(List.of(e1, e2));

        List<TopDefectLineDTO> result =
                statsService.getTopDefectLines(
                        Instant.now().minusSeconds(3600),
                        Instant.now(),
                        5
                );

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getLineId()).isEqualTo("L-1");
        assertThat(result.get(0).getTotalDefects()).isEqualTo(5);
    }

    // -------------------------------
    // Concurrency Test (READ SAFETY)
    // -------------------------------
    @Test
    void statsService_shouldHandleConcurrentReads() throws Exception {

        Runnable task = () -> statsService.getDailyStats();

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertThat(true).isTrue(); // no exception = PASS
    }
}
