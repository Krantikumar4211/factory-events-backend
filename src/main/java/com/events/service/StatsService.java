package com.events.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.events.dto.DailyStatsDTO;
import com.events.dto.MachineStatsDTO;
import com.events.dto.TopDefectLineDTO;
import com.events.entity.EventEntity;
import com.events.repository.EventRepository;

@Service
public class StatsService {

    private final EventRepository eventRepository;

    public StatsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // ---------------- DAILY STATS ----------------

    public List<DailyStatsDTO> getDailyStats() {

        return eventRepository.fetchDailyStats()
                .stream()
                .map(row -> {
                    LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
                    long total = (long) row[1];
                    long saved = (long) row[2];
                    long skipped = total - saved;
                    return new DailyStatsDTO(date, saved, skipped, total);
                })
                .collect(Collectors.toList());
    }

    // ---------------- MACHINE STATS ----------------

    public MachineStatsDTO getMachineStats(
            String machineId, Instant start, Instant end) {

        List<EventEntity> events =
                eventRepository.findEventsForMachine(machineId, start, end);

        long eventsCount = events.size();

        long defectsCount = events.stream()
                .filter(e -> e.getDefectCount() != null)
                .mapToLong(EventEntity::getDefectCount)
                .sum();

        double windowHours =
                Duration.between(start, end).toSeconds() / 3600.0;

        double avgDefectRate =
                windowHours > 0 ? defectsCount / windowHours : 0.0;

        String status =
                avgDefectRate < 2.0 ? "Healthy" : "Warning";

        return new MachineStatsDTO(
                machineId,
                start,
                end,
                eventsCount,
                defectsCount,
                avgDefectRate,
                status
        );
    }

    // ---------------- TOP DEFECT LINES ----------------

    public List<TopDefectLineDTO> getTopDefectLines(
            Instant from, Instant to, int limit) {

        List<Object[]> rows =
                eventRepository.findTopDefectLines(from, to, PageRequest.of(0, limit));

        return rows.stream().map(row -> {

            String lineId = (String) row[0];
            long eventCount = (long) row[1];
            long totalDefects = row[2] != null ? (long) row[2] : 0;

            double defectsPercent =
                    eventCount > 0
                            ? Math.round((totalDefects * 10000.0 / eventCount)) / 100.0
                            : 0.0;

            return new TopDefectLineDTO(
                    lineId,
                    totalDefects,
                    eventCount,
                    defectsPercent
            );
        }).toList();
    }
}
