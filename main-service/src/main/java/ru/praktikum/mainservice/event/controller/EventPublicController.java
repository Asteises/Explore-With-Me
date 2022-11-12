package ru.praktikum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.EventShortDto;
import ru.praktikum.mainservice.event.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventPublicController {

    public final EventService eventService;

    //TODO Сделать вместе
    /*
    GET EVENTS - Получение событий с возможностью фильтрации
        Обратите внимание:
            это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события;
            текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв;
            если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени;
            информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие;
            информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики;
     */
    @GetMapping()
    public List<EventShortDto> getAllPublicEvents(@RequestParam String text,
                                                  @RequestParam int[] categories,
                                                  @RequestParam Boolean paid,
                                                  @RequestParam String rangeStart,
                                                  @RequestParam String rangeEnd,
                                                  @RequestParam Boolean onlyAvailable,
                                                  @RequestParam String sort,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получаем все события с учетом фильтрации: text={}, categories={}, paid={}, rangeStart={}, " +
                        "rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.getAllPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    /*
    Получение подробной информации об опубликованном событии по его идентификатору
        Обратите внимание:
            событие должно быть опубликовано;
            информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов;
            информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики;
     */
    @GetMapping("/{id}")
    public EventFullDto getPublicEventById(@PathVariable long id) {
        log.info("Получаем событие: id={}", id);
        return eventService.getPublicEventById(id);
    }
}
