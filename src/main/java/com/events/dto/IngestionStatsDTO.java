package com.events.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IngestionStatsDTO {

    private long accepted;
    private long deduped;
    private long updated;
    private long rejected;
    private long saved;
    private long skipped;
    private long total;
}
