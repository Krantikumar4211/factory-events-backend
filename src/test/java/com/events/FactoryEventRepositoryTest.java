package com.events;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.events.entity.EventEntity;
import com.events.repository.EventRepository;

@DataJpaTest
class FactoryEventRepositoryTest {

    @Autowired
    private EventRepository repository;

    @Test
    void duplicateEventShouldFailAtRepositoryLevel() {

        EventEntity event1 = createValidEvent("EVT-1001");
        EventEntity event2 = createValidEvent("EVT-1001"); // duplicate

        repository.saveAndFlush(event1);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(event2)
        );
    }

    private EventEntity createValidEvent(String eventId) {

        Instant now = Instant.now();

        EventEntity e = new EventEntity();
        e.setEventId(eventId);
        e.setMachineId("M-01");
        e.setLineId("LINE-A");
        e.setEventTime(now);
        e.setReceivedTime(now);
        e.setDurationMs(100L);
        e.setDefectCount(1);
        e.setPayloadHash("hash-" + eventId);

        return e;
    }
}
