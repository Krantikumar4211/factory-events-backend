package com.events.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.events.dto.EventRequestDTO;
import com.events.entity.EventEntity;
import com.events.repository.EventRepository;
import com.events.util.HashUtil;
import com.events.validation.EventValidator;
import com.events.validation.ValidationResult;

@Service
public class EventIngestService {

    private final EventRepository eventRepository;

    public EventIngestService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public BatchResult ingestBatch(List<EventRequestDTO> events) {

        BatchResult result = new BatchResult();

        for (EventRequestDTO dto : events) {

            // 1️⃣ Validation
        	ValidationResult validation = EventValidator.validate(dto);

        	if (!validation.isValid()) {
        		result.incrementRejected();
        	    result.addRejection(dto.getEventId(), validation.getReason());
        	    continue;
        	}


            // 2️⃣ Prepare data
            Instant receivedTime = Instant.now();
            String payloadHash = buildPayloadHash(dto);

            Optional<EventEntity> existingOpt =
                    eventRepository.findByEventId(dto.getEventId());

            // 3️⃣ New event
            if (existingOpt.isEmpty()) {
                EventEntity entity = toEntity(dto, receivedTime, payloadHash);
                eventRepository.save(entity);
                result.incrementAccepted();
                continue;
            }

            // 4️⃣ Existing event
            EventEntity existing = existingOpt.get();

            // Same payload → dedupe
            if (payloadHash.equals(existing.getPayloadHash())) {
            	result.incrementDeduped();
                continue;
            }

            // Different payload → compare receivedTime
            if (receivedTime.isAfter(existing.getReceivedTime())) {
                updateEntity(existing, dto, receivedTime, payloadHash);
                eventRepository.save(existing);
                result.incrementUpdated();
            } else {
                // Older update → ignore
            	result.incrementDeduped();
            }
        }

        return result;
    }

    // ----------------- Helper Methods -----------------

    private String validate(EventRequestDTO dto) {

        if (dto.getDurationMs() < 0)
            return "INVALID_DURATION";

        if (dto.getDurationMs() > Duration.ofHours(6).toMillis())
            return "DURATION_TOO_LARGE";

        if (dto.getEventTime().isAfter(Instant.now().plus(Duration.ofMinutes(15))))
            return "EVENT_TIME_IN_FUTURE";

        return null;
    }

    private String buildPayloadHash(EventRequestDTO dto) {
        String payload = dto.getEventId()
                + dto.getMachineId()
                + dto.getLineId()
                + dto.getEventTime()
                + dto.getDurationMs()
                + dto.getDefectCount();

        return HashUtil.sha256(payload);
    }

    private EventEntity toEntity(EventRequestDTO dto,
                                 Instant receivedTime,
                                 String payloadHash) {

        EventEntity entity = new EventEntity();
        entity.setEventId(dto.getEventId());
        entity.setMachineId(dto.getMachineId());
        entity.setLineId(dto.getLineId());
        entity.setEventTime(dto.getEventTime());
        entity.setReceivedTime(receivedTime);
        entity.setDurationMs(dto.getDurationMs());
        entity.setDefectCount(dto.getDefectCount());
        entity.setPayloadHash(payloadHash);
        return entity;
    }

    private void updateEntity(EventEntity entity,
                              EventRequestDTO dto,
                              Instant receivedTime,
                              String payloadHash) {

        entity.setMachineId(dto.getMachineId());
        entity.setLineId(dto.getLineId());
        entity.setEventTime(dto.getEventTime());
        entity.setReceivedTime(receivedTime);
        entity.setDurationMs(dto.getDurationMs());
        entity.setDefectCount(dto.getDefectCount());
        entity.setPayloadHash(payloadHash);
    }
}

