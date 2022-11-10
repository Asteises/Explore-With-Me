package ru.praktikum.mainservice.event.service;

import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.event.model.Event;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;
import ru.praktikum.mainservice.request.model.dto.UpdateEventRequest;

import java.util.List;

@Service
public interface EventService {

    EventFullDto createEvent(long userId, NewEventDto newEventDto);

    EventFullDto updateEventByCurrentUser(long userId, UpdateEventRequest updateEventRequest);

    List<EventFullDto> getAllOwnEvents(long userId, Integer from, Integer size);

    EventFullDto getOwnEventById(long userId, long eventId);

    EventFullDto cancelOwnEvent(long userId, long eventId);

    Event checkEventAvailableInDb(long eventId);
}
