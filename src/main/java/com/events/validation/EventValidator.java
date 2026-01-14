package com.events.validation;

import java.time.Duration;
import java.time.Instant;

import com.events.dto.EventRequestDTO;

public class EventValidator {

    private EventValidator() {}

    public static ValidationResult validate(EventRequestDTO dto) {

        if (dto.getDurationMs() == null)
            return ValidationResult.fail("DURATION_MISSING");

        if (dto.getDurationMs() < 0)
            return ValidationResult.fail("INVALID_DURATION");

        if (dto.getDurationMs() > Duration.ofHours(6).toMillis())
            return ValidationResult.fail("DURATION_TOO_LARGE");

        if (dto.getEventTime() == null)
            return ValidationResult.fail("EVENT_TIME_MISSING");

        if (dto.getEventTime()
                .isAfter(Instant.now().plus(Duration.ofMinutes(15))))
            return ValidationResult.fail("EVENT_TIME_IN_FUTURE");

        return ValidationResult.ok();
    }
}
