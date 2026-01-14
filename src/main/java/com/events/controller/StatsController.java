package com.events.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.events.dto.DailyStatsDTO;
import com.events.dto.MachineStatsDTO;
import com.events.dto.TopDefectLineDTO;
import com.events.service.StatsService;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    // ----------------------------------------------------
    // 1️⃣ DAILY INGESTION STATS
    // ----------------------------------------------------
    // GET /stats/daily
    @GetMapping("/daily")
    public List<DailyStatsDTO> getDailyStats() {
        return statsService.getDailyStats();
    }

    // ----------------------------------------------------
    // 2️⃣ MACHINE HEALTH STATS
    // ----------------------------------------------------
    // GET /stats/machine?machineId=M-1&start=...&end=...
    @GetMapping("/machine")
    public MachineStatsDTO getMachineStats(
            @RequestParam String machineId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant start,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant end) {

        return statsService.getMachineStats(machineId, start, end);
    }

    // ----------------------------------------------------
    // 3️⃣ TOP DEFECT LINES
    // ----------------------------------------------------
    // GET /stats/top-defect-lines?from=...&to=...&limit=5
    @GetMapping("/top-defect-lines")
    public List<TopDefectLineDTO> getTopDefectLines(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant from,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant to,
            @RequestParam(defaultValue = "5")
            int limit) {

        return statsService.getTopDefectLines(from, to, limit);
    }
}
