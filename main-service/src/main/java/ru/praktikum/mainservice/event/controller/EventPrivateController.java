package ru.praktikum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;
import ru.praktikum.mainservice.event.service.EventService;
import ru.praktikum.mainservice.request.model.dto.UpdateEventRequest;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class EventPrivateController {

    private final EventService eventService;

    /*
    POST EVENT - Добавление нового события:
        Обратите внимание:
            дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента;
    */
    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(@PathVariable long userId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Пользователь userId={} создает новое событие {}", userId, newEventDto.toString());
        return eventService.createEvent(userId, newEventDto);
    }

    /*
    PATCH EVENT - Изменение события добавленного текущим пользователем:
        Обратите внимание:
            изменить можно только отмененные события или события в состоянии ожидания модерации
            если редактируется отменённое событие, то оно автоматически переходит в состояние ожидания модерации
            дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
    */
    @PatchMapping("/{userId}/events")
    public EventFullDto updateEventByCurrentUser(@PathVariable long userId,
                                                 @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Пользователь userId={} обновляет событие {}", userId, updateEventRequest.toString());
        return eventService.updateEventByCurrentUser(userId, updateEventRequest);
    }
}
