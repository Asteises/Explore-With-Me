package ru.praktikum.mainservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.praktikum.mainservice.event.model.Event;

import java.util.Optional;

@Repository
public interface EventStorage extends JpaRepository<Event, Long> {
    Page<Event> findEventByInitiator_Id(long userId, Pageable pageable);

    Optional<Event> findEventByCategory_Id(long catId);
}
