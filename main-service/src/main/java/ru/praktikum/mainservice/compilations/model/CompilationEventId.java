package ru.praktikum.mainservice.compilations.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;

@Embeddable //TODO ?
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CompilationEventId implements Serializable {
    private static final long serialVersionUID = 1300120015012804277L;
    @javax.validation.constraints.NotNull
    @Column(name = "comp_id", nullable = false)
    private Long compId;

    @javax.validation.constraints.NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;
}