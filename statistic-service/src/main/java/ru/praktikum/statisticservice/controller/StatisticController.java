package ru.praktikum.statisticservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.praktikum.statisticservice.model.dto.EndpointHitDto;
import ru.praktikum.statisticservice.model.dto.ViewStatsDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stat")
public class StatisticController {

    /*
    POST - Сохранение информации о том, что к эндпоинту был запрос
        Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
        Название сервиса, uri и ip пользователя указаны в теле запроса.
     */
    @PostMapping("/hit")
    public void saveRequestInfo(EndpointHitDto endpointHitDto) {

        log.info("Сохранение информации о том, что к эндпоинту uri={} был запрос: app={}",
                endpointHitDto.getUri(), endpointHitDto.getApp());
    }

    /*
    GET - Получение статистики по посещениям. Обратите внимание: значение даты и времени нужно закодировать
    (например используя java.net.URLEncoder.encode)
     */
    @GetMapping
    public List<ViewStatsDto> getEventsStatInfo(@RequestParam String start,
                                                @RequestParam String end,
                                                @RequestParam String[] uris,
                                                @RequestParam(defaultValue = "false") Boolean unique) {

        log.info("Получаем статистику на события: uris={} с параметрами start={}, end={}, unique={}",
                uris, start, end, unique);
        return null;
    }

    @GetMapping("/{eventId}")
    public EndpointHitDto getEventStatInfo(@PathVariable long eventId) {

        log.info("Получаем статистику на событие: eventId={}", eventId);
        return null;
    }
}
