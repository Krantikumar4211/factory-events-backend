package com.events.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.events.service.BatchResult;
import com.events.service.EventIngestService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EventIngestController.class)
class EventIngestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventIngestService ingestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void ingestBatch_shouldReturnAcceptedAndSummary() throws Exception {

        // Arrange
        BatchResult result = new BatchResult();
        result.incrementAccepted();
        result.incrementAccepted();   // accepted = 2
        result.incrementDeduped();    // deduped = 1

        when(ingestService.ingestBatch(anyList()))
                .thenReturn(result);

        String requestJson = """
        [
          {
            "eventId": "EVT-1",
            "machineId": "M-1",
            "lineId": "L-1",
            "eventTime": "2026-01-14T10:00:00Z",
            "durationMs": 100,
            "defectCount": 1
          }
        ]
        """;

        // Act + Assert
        mockMvc.perform(post("/events/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.accepted").value(2))
            .andExpect(jsonPath("$.deduped").value(1));
    }
}
