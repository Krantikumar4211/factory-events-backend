package com.events.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MachineStatsDTO {

    private String machineId;
    private Instant start;
    private Instant end;

    private long eventsCount;
    private long defectsCount;
    private double avgDefectRate;
    private String status;
}

