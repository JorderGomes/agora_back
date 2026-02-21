package com.jorder.agora.repository;

import com.jorder.agora.model.Event;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class EventSpecification {

    public static Specification<Event> hasTitle(String title) {
        return (root, query, cb) ->
                title == null ? null : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Event> hasOrganizer(UUID organizerId) {
        return (root, query, cb) ->
                organizerId == null ? null : cb.equal(root.get("organizer").get("id"), organizerId);
    }

}
