package ru.praktikum.mainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.praktikum.mainservice.event.model.EventState;

import java.util.Optional;

@Repository
public interface EventStateStorage extends JpaRepository<EventState, Long> {

    Optional<EventState> findEventStateByEvent_Id(long eventId);
}
