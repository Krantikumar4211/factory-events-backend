package com.events.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(
    name = "events",
    uniqueConstraints = @UniqueConstraint(columnNames = "event_id"),
    indexes = {
        @Index(name = "idx_event_time", columnList = "event_time"),
        @Index(name = "idx_machine_time", columnList = "machine_id,event_time")
    }
)
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @Column(name = "machine_id")
    private String machineId;

    @Column(name = "line_id")
    private String lineId;

    @Column(name = "event_time", nullable = false)
    private Instant eventTime;

    @Column(name = "received_time", nullable = false)
    private Instant receivedTime;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(name = "defect_count")
    private Integer defectCount;

    @Column(name = "payload_hash", length = 64)
    private String payloadHash;

	public void setEventType(String string) {
		// TODO Auto-generated method stub
		
	}

    // getters & setters
}

