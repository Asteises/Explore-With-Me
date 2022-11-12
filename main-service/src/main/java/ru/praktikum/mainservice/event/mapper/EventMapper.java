package ru.praktikum.mainservice.event.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.Location;
import ru.praktikum.mainservice.event.enums.StateEnum;
import ru.praktikum.mainservice.category.mapper.CategoryMapper;
import ru.praktikum.mainservice.event.model.Event;
import ru.praktikum.mainservice.event.model.EventState;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.EventShortDto;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;
import ru.praktikum.mainservice.request.model.dto.UpdateEventRequest;
import ru.praktikum.mainservice.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class EventMapper {

    private static final DateTimeFormatter FORMATTER_EVENT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto newEventDto) {
        Event event = new Event();
        // id - генерируется БД;

        //Приходит "eventDate": "2024-12-31 15:10:05" - форматируем чтобы получить LocalDateTime;
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER_EVENT_DATE);
        event.setEventDate(eventDate);

        event.setTitle(newEventDto.getTitle());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setPaid(newEventDto.getPaid());
        event.setCreatedOn(LocalDateTime.now());
        event.setLocationLat(newEventDto.getLocation().getLat().doubleValue());
        event.setLocationLon(newEventDto.getLocation().getLon().doubleValue());
        event.setParticipantLimit(newEventDto.getParticipantLimit().longValue());
        // Категорию проставляем в сервисе
        event.setCategory(null);
        // Пользователь (инициатор) - приходит в контроллере, проверяем его и сетим в сервисе;
        event.setInitiator(null);
        //
        event.setPublishedOn(null);

        log.info("Мапим NewEventDto в Event: {}", event);
        return event;
    }

    public static EventFullDto fromEventToEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();

        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setParticipantLimit(event.getParticipantLimit().intValue());
        eventFullDto.setCategory(CategoryMapper.categoryToCategoryDto(event.getCategory()));
        eventFullDto.setInitiator(UserMapper.userToUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(new Location(
                event.getLocationLat().floatValue(),
                event.getLocationLon().floatValue()));
        //форматируем LocalDateTime to String
        eventFullDto.setCreatedOn(event.getCreatedOn().format(FORMATTER_EVENT_DATE));
        eventFullDto.setEventDate(event.getEventDate().format(FORMATTER_EVENT_DATE));

        eventFullDto.setPublishedOn(null);
        eventFullDto.setState(null);
        eventFullDto.setViews(null);
        eventFullDto.setConfirmedRequests(null);

        log.info("Мапим Event в EventFullDto: {}", eventFullDto);
        return eventFullDto;
    }

    public static EventShortDto fromEventToEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();

        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setCategory(CategoryMapper.categoryToCategoryDto(event.getCategory()));
        eventShortDto.setInitiator(UserMapper.userToUserShortDto(event.getInitiator()));
        eventShortDto.setEventDate(event.getEventDate().format(FORMATTER_EVENT_DATE));

        eventShortDto.setConfirmedRequests(null);
        eventShortDto.setViews(null);

        log.info("Мапим Event в EventShortDto: {}", eventShortDto);
        return eventShortDto;
    }

    public static void fromUpdateEventRequestToEvent(Event event, UpdateEventRequest updateEventRequest) {
        // Далее изменяем те данные, которые пришли в newEventDto;
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEventRequest.getEventDate(), FORMATTER_EVENT_DATE);
            event.setEventDate(eventDate);
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit().longValue());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
    }

    public static EventState fromEventToEventState(Event event) {
        EventState eventState = new EventState();
        eventState.setEvent(event);
        eventState.setState(StateEnum.PENDING.toString());
        eventState.setCreatedOn(LocalDateTime.now());
        return eventState;
    }
}
