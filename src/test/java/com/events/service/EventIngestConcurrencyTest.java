package com.events.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.events.dto.EventRequestDTO;
import com.events.repository.EventRepository;

@SpringBootTest
@Transactional
class EventIngestConcurrencyTest {

    @Autowired
    private EventIngestService ingestService;

    @Autowired
    private EventRepository eventRepository;

    @Test
    void concurrentBatchIngest_shouldNotCreateDuplicates() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(5);

        EventRequestDTO dto = new EventRequestDTO(
        	    "EVT-CONCURRENT",
        	    "M-9",
        	    "L-9",
        	    Instant.now(),
        	    Long.valueOf(100),   // ✅ correct
        	    1
        	);


        Runnable task = () ->
                ingestService.ingestBatch(List.of(dto));

        for (int i = 0; i < 10; i++) {
            executor.submit(task);
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        long count =
                eventRepository.countByEventId("EVT-CONCURRENT");

        assertThat(count).isEqualTo(1);
    }
}
