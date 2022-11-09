package ru.praktikum.mainservice.event.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.Location;
import ru.praktikum.mainservice.StateEnum;
import ru.praktikum.mainservice.event.model.Event;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;
import ru.praktikum.mainservice.user.model.dto.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto) {
        Event event = new Event();

        event.setTitle(newEventDto.getTitle());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        // Категория
        // Пользователь (инициатор)
        //"eventDate": "2024-12-31 15:10:05"
        //event.setCreatedOn(LocalDateTime.now().toInstant("Europe"));
        event.setLocationLat(newEventDto.);

        @JsonProperty("confirmedRequests")
        private Long confirmedRequests = null;

        @JsonProperty("createdOn")
        private String createdOn = null;

        @JsonProperty("description")
        private String description = null;

        @JsonProperty("eventDate")
        private String eventDate = null;

        @JsonProperty("id")
        private Long id = null;

        @JsonProperty("initiator")
        private UserShortDto initiator = null;

        @JsonProperty("location")
        private Location location = null;

        @JsonProperty("paid")
        private Boolean paid = null;

        @JsonProperty("participantLimit")
        private Integer participantLimit = 0;

        @JsonProperty("publishedOn")
        private String publishedOn = null;

        @JsonProperty("requestModeration")
        private Boolean requestModeration = true;

        @JsonProperty("state")
        private StateEnum state = null;

        @JsonProperty("title")
        private String title = null;

        @JsonProperty("views")
        private Long views = null;
    }
}
