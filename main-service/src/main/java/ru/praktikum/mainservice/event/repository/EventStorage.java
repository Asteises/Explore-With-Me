package ru.praktikum.mainservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.praktikum.mainservice.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventStorage extends JpaRepository<Event, Long> {
    Page<Event> findEventByInitiator_Id(long userId, Pageable pageable);

    Optional<Event> findEventByCategory_Id(long catId);

    Page<Event> findAllByIdIn(List<Long> eventIds, Pageable pageable);

    List<Event> findEventsByIdIn(List<Long> eventIds);

    Page<Event> findEventsByAnnotationContainingIgnoreCaseAndDescriptionContainingIgnoreCaseAndCategory_IdInAndPaidAndEventDateBetweenOrderByEventDateDesc(
            String annotation,
            String description,
            List<Long> catIds,
            Boolean paid,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable);
}
