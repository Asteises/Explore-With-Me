package ru.praktikum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    @Override
    public EventFullDto createEvent(long userId, NewEventDto newEventDto) {
        return null;
    }
}
