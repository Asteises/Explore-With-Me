package ru.praktikum.mainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.praktikum.mainservice.event.model.EventState;

@Repository
public interface EventStateStorage extends JpaRepository<EventState, Long> {

    EventState findEventStateByEvent_Id(long eventId);
}
