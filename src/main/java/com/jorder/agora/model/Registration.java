package com.jorder.agora.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registrations",
        indexes = {
        @Index(name = "idx_reg_event_id", columnList = "event_id"),
        @Index(name = "idx_reg_notified", columnList = "notificationSent")
})
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean present;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean notificationSent;

}
