package ru.praktikum.mainservice.event.model;

import lombok.Getter;
import lombok.Setter;
import ru.praktikum.mainservice.category.model.Category;
import ru.praktikum.mainservice.user.model.User;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @javax.validation.constraints.Size(max = 120)
    @javax.validation.constraints.NotNull
    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @javax.validation.constraints.Size(max = 2000)
    @javax.validation.constraints.NotNull
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;

    @javax.validation.constraints.Size(max = 7000)
    @javax.validation.constraints.NotNull
    @Column(name = "description", nullable = false, length = 7000)
    private String description;

    @javax.validation.constraints.NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @javax.validation.constraints.NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @javax.validation.constraints.NotNull
    @Column(name = "event_date", nullable = false)
    private Instant eventDate;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "location_lat")
    private Double locationLat;

    @Column(name = "location_lon")
    private Double locationLon;

    @javax.validation.constraints.NotNull
    @Column(name = "paid", nullable = false)
    private Boolean paid = false;

    @Column(name = "participant_limit")
    private Long participantLimit;

    @Column(name = "published_on")
    private Instant publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;
}