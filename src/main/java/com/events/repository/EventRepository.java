package com.events.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.events.entity.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Optional<EventEntity> findByEventId(String eventId);
     
    @Query("""
        SELECT e FROM EventEntity e
        WHERE e.machineId = :machineId
          AND e.eventTime >= :start
          AND e.eventTime < :end
    """)
    List<EventEntity> findEventsForMachine(
            @Param("machineId") String machineId,
            @Param("start") Instant start,
            @Param("end") Instant end);
    
    @Query("""
    	    SELECT e.lineId,
    	           COUNT(e),
    	           SUM(CASE WHEN e.defectCount >= 0 THEN e.defectCount ELSE 0 END)
    	    FROM EventEntity e
    	    WHERE e.eventTime >= :from
    	      AND e.eventTime < :to
    	      AND e.lineId IS NOT NULL
    	    GROUP BY e.lineId
    	    ORDER BY SUM(CASE WHEN e.defectCount >= 0 THEN e.defectCount ELSE 0 END) DESC
    	""")
    	List<Object[]> findTopDefectLines(
    	        @Param("from") Instant from,
    	        @Param("to") Instant to,
    	        Pageable pageable);

    	@Query("""
    	        SELECT DATE(e.receivedTime),
    	               COUNT(e),
    	               SUM(CASE WHEN e.payloadHash IS NOT NULL THEN 1 ELSE 0 END)
    	        FROM EventEntity e
    	        GROUP BY DATE(e.receivedTime)
    	        ORDER BY DATE(e.receivedTime)
    	    """)
    	    List<Object[]> fetchDailyStats();

    	    long count();
    	    
    	    long countByEventId(String eventId);

    	
}

