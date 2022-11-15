package ru.praktikum.mainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.praktikum.mainservice.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventStateStorage extends JpaRepository<EventState, Long> {

    Optional<EventState> findEventStateByEvent_Id(long eventId);

    List<EventState> findAllByEvent_Initiator_IdInAndEvent_Category_IdInAndEvent_EventDateBetweenAndStateIn(
            List<Long> initiatorIds,
            List<Long> categoryIds,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            List<String> state);
}
