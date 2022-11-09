package ru.praktikum.mainservice.event.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.Location;
import ru.praktikum.mainservice.category.mapper.CategoryMapper;
import ru.praktikum.mainservice.event.model.Event;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.EventShortDto;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;
import ru.praktikum.mainservice.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto) {
        Event event = new Event();

        String stringDate = newEventDto.getEventDate();
        String pattern = "uuuu-M-d hh:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(stringDate, dateTimeFormatter);

        // id - проставляется в БД
        event.setTitle(newEventDto.getTitle());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setCategory(null);
        // Пользователь (инициатор) - приходит в контроллере
        event.setInitiator(null);
        //"eventDate": "2024-12-31 15:10:05"
        event.setEventDate(localDateTime.toInstant());
        //event.setCreatedOn(LocalDateTime.now().toInstant("Europe"));
        event.setCreatedOn(null);
        event.setLocationLat(newEventDto.getLocation().getLat().doubleValue());
        event.setLocationLon(newEventDto.getLocation().getLon().doubleValue());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit().longValue());
        event.setPublishedOn(null);
        event.setRequestModeration(newEventDto.getRequestModeration());
        return event;
    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();

        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.categoryToCategoryDto(event.getCategory()));
        eventFullDto.setInitiator(UserMapper.userToUserShortDto(event.getInitiator()));
        eventFullDto.setCreatedOn(null);
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(null);
        eventFullDto.setLocation(new Location(event.getLocationLat().floatValue(), event.getLocationLon().floatValue()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit().intValue());
        eventFullDto.setPublishedOn(null);
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setState(null);
        eventFullDto.setViews(null);
        eventFullDto.setConfirmedRequests(null);

        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();

        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.categoryToCategoryDto(event.getCategory()));
        eventShortDto.setConfirmedRequests(null);
        eventShortDto.setEventDate(null);
        eventShortDto.setInitiator(UserMapper.userToUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(null);

        return eventShortDto;
    }
}
