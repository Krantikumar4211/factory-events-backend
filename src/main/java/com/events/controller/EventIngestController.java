package com.events.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.events.dto.EventRequestDTO;
import com.events.service.BatchResult;
import com.events.service.EventIngestService;

@RestController
@RequestMapping("/events")
public class EventIngestController {

    private final EventIngestService ingestService;

    public EventIngestController(EventIngestService ingestService) {
        this.ingestService = ingestService;
    }

    /**
     * Batch ingest endpoint
     * POST /events/batch
     */
    @PostMapping("/batch")
    public ResponseEntity<BatchResult> ingestBatch(
            @RequestBody List<EventRequestDTO> events) {

        BatchResult result = ingestService.ingestBatch(events);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(result);
    }
}

