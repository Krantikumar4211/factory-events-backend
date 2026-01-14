package com.events;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.events.dto.EventRequestDTO;
import com.events.service.BatchResult;
import com.events.service.EventIngestService;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ww {

    @Autowired
    private EventIngestService ingestService;

    @Test
    void batchIngest_shouldHandleDuplicatesCorrectly() {

        Instant now = Instant.now();

        EventRequestDTO e1 = new EventRequestDTO();
        e1.setEventId("EVT-1");
        e1.setMachineId("M-1");
        e1.setLineId("L-1");
        e1.setEventTime(now);
        e1.setDurationMs(100L);
        e1.setDefectCount(1);

        EventRequestDTO e2 = new EventRequestDTO();
        e2.setEventId("EVT-1"); // duplicate
        e2.setMachineId("M-1");
        e2.setLineId("L-1");
        e2.setEventTime(now);
        e2.setDurationMs(100L);
        e2.setDefectCount(1);

        EventRequestDTO e3 = new EventRequestDTO();
        e3.setEventId("EVT-2");
        e3.setMachineId("M-2");
        e3.setLineId("L-2");
        e3.setEventTime(now);
        e3.setDurationMs(200L);
        e3.setDefectCount(2);

        BatchResult result = ingestService.ingestBatch(List.of(e1, e2, e3));

        assertThat(result.getTotal()).isEqualTo(3);
        assertThat(result.getSaved()).isEqualTo(2);
        assertThat(result.getSkipped()).isEqualTo(1);
    }

}
