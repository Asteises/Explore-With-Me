package ru.praktikum.mainservice.event.service;

import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;
import ru.praktikum.mainservice.request.model.dto.UpdateEventRequest;

@Service
public interface EventService {

    EventFullDto createEvent(long userId, NewEventDto newEventDto);

    EventFullDto updateEventByCurrentUser(long userId, UpdateEventRequest updateEventRequest);
}
