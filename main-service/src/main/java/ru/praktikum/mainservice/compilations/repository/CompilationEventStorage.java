package ru.praktikum.mainservice.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.praktikum.mainservice.compilations.model.Compilation;
import ru.praktikum.mainservice.compilations.model.CompilationEvent;
import ru.praktikum.mainservice.event.model.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompilationEventStorage extends JpaRepository<CompilationEvent, Long> {

    Optional<CompilationEvent> findByEventAndAndComp(Event event, Compilation comp);

    void deleteCompilationEventByEventAndComp(Event event, Compilation compilation);

    List<CompilationEvent> findAllByComp(Compilation compilation);

    Optional<CompilationEvent> findCompilationEventByComp(Compilation compilation);
}
