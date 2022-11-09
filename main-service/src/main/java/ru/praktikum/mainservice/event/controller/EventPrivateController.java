package ru.praktikum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;
import ru.praktikum.mainservice.event.service.EventService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class EventPrivateController {

    private final EventService eventService;

    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(@PathVariable long userId,
                                    @RequestBody NewEventDto newEventDto) {
        log.info("Пользователь userId={} создает новое событие {}", userId, newEventDto.toString());
        return eventService.createEvent(userId, newEventDto);
    }
}
