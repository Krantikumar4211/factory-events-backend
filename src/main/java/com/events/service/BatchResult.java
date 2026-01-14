package com.events.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class BatchResult {

    private int accepted;
    private int deduped;
    private int updated;
    private int rejected;

    private final List<Rejection> rejections = new ArrayList<>();

    // ===== increment helpers =====
    public void incrementAccepted() {
        accepted++;
    }

    public void incrementDeduped() {
        deduped++;
    }

    public void incrementUpdated() {
        updated++;
    }

    public void incrementRejected() {
        rejected++;
    }

    // ===== derived values =====
    public int getTotal() {
        return accepted + deduped + updated + rejected;
    }

    public int getSaved() {
        return accepted + updated;
    }

    public int getSkipped() {
        return deduped + rejected;
    }

    // ===== rejection handling =====
    public void addRejection(String eventId, String reason) {
        rejections.add(new Rejection(eventId, reason));
    }

    public record Rejection(String eventId, String reason) {}
}
