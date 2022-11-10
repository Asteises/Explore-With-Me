package ru.praktikum.mainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.praktikum.mainservice.event.model.Event;

@Repository
public interface EventStorage extends JpaRepository<Event, Long> {

}
