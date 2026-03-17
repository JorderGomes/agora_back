package com.jorder.agora.repository;

import com.jorder.agora.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, UUID> {

    Optional<Registration> findByEventIdAndUserId(UUID eventId, UUID userId);

    boolean existsByEventIdAndUserId(UUID eventId, UUID userId);

    @Query("SELECT r FROM Registration r JOIN r.event e " +
            "WHERE e.eventDate BETWEEN :start AND :end " +
            "AND r.notificationSent = false")
    List<Registration> findPendingNotifications(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
