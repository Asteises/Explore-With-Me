package ru.praktikum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.service.EventService;
import ru.praktikum.mainservice.event.model.dto.AdminUpdateEventRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {

    private final EventService eventService;

    /*
    GET EVENT ADMIN - Поиск событий
        Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия;
     */
    @GetMapping
    public List<EventFullDto> searchEvents(@RequestParam Long[] users,
                                           @RequestParam String[] states,
                                           @RequestParam Long[] categories,
                                           @RequestParam String rangeStart,
                                           @RequestParam String rangeEnd,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получаем все события с учетом параметров: users={}, states={}, categories={}, " +
                "rangeStart={}, rangeEnd={}, from={}, size={}",
                Arrays.toString(users), Arrays.toString(states), Arrays.toString(categories), rangeStart, rangeEnd, from, size);
        return eventService.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

        /*
        PUT EVENT ADMIN - Редактирование события
            Редактирование данных любого события администратором. Валидация данных не требуется;
        */
    @PutMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable long eventId,
                                           @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Админ редактирует событие: eventId={}", eventId);
        return eventService.updateEventByAdmin(eventId, adminUpdateEventRequest);
    }
}
