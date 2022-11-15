package ru.praktikum.mainservice.compilations.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.praktikum.mainservice.event.model.Event;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "compilation_events")
public class CompilationEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
//    private CompilationEventId id;

//    @MapsId("compId")
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comp_id", nullable = false)
    private Compilation comp;

//    @MapsId("eventId")
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
}