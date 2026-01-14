package com.events.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventRequestDTO {

    private String eventId;
    private String machineId;
    private String lineId;
    private Instant eventTime;
    private Long durationMs;   // ✅ FIXED
    private Integer defectCount;
}
