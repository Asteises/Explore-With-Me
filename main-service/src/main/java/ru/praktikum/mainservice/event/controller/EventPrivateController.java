package ru.praktikum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;
import ru.praktikum.mainservice.event.service.EventService;
import ru.praktikum.mainservice.request.model.dto.UpdateEventRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

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
            дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента;
    */
    @PatchMapping("/{userId}/events")
    public EventFullDto updateEventByCurrentUser(@PathVariable long userId,
                                                 @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Пользователь userId={} обновляет событие {}", userId, updateEventRequest.toString());
        return eventService.updateEventByCurrentUser(userId, updateEventRequest);
    }

    /*
    GET EVENTS - Получение событий добавленных текущим пользователем:
    */
    @GetMapping("/{userId}/events")
    public List<EventFullDto> getAllOwnEvents(@PathVariable long userId,
                                        @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                        @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Пользователь userId={} получает все свои созданные события", userId);
        return eventService.getAllOwnEvents(userId, from, size);
    }

    /*
    GET EVENT - Получение полной информации о событии добавленном текущим пользователем:
    */
    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getOwnEventById(@PathVariable long userId,
                                 @PathVariable long eventId) {
        log.info("Пользователь userId={} получает свое событие eventId={}", userId, eventId);
        return eventService.getOwnEventById(userId, eventId);
    }

    /*
    PATCH EVENT - Отмена события добавленного текущим пользователем:
        Обратите внимание:
            Отменить можно только событие в состоянии ожидания модерации;
     */
    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelOwnEvent(@PathVariable long userId,
                                       @PathVariable long eventId) {
        log.info("Пользователь userId={} отменяет свое событие eventId={}", userId, eventId);
        return eventService.cancelOwnEvent(userId, eventId);
    }
}
