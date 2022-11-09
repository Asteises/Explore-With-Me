package ru.praktikum.mainservice.event.service;

import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;

@Service
public interface EventService {

    EventFullDto createEvent(long userId, NewEventDto newEventDto);
}
