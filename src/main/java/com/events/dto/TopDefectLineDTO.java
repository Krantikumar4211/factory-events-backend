package com.events.dto;

import lombok.Data;

@Data
public class TopDefectLineDTO {

    private String lineId;
    private long totalDefects;
    private long eventCount;
    private double defectsPercent;

    public TopDefectLineDTO(String lineId,
                            long totalDefects,
                            long eventCount,
                            double defectsPercent) {
        this.lineId = lineId;
        this.totalDefects = totalDefects;
        this.eventCount = eventCount;
        this.defectsPercent = defectsPercent;
    }

    // getters & setters
}

