package ru.praktikum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.event.mapper.EventMapper;
import ru.praktikum.mainservice.event.model.Event;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;
import ru.praktikum.mainservice.event.repository.EventStorage;
import ru.praktikum.mainservice.exception.NotFoundException;
import ru.praktikum.mainservice.user.model.User;
import ru.praktikum.mainservice.user.repository.UserStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventStorage eventStorage;
    private final UserStorage userStorage;

    @Override
    public EventFullDto createEvent(long userId, NewEventDto newEventDto) {
        User initiator = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь не найден: userId=%s", userId)));
        Event event = EventMapper.toEvent(newEventDto);
        event.setInitiator(initiator);
        event = eventStorage.save(event);
        log.info("Создано новое событие: {}", event);
        return EventMapper.toEventFullDto(event);
    }
}
