package com.events.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyStatsDTO {
    private LocalDate date;
    private long saved;
    private long skipped;
    private long total;
}
