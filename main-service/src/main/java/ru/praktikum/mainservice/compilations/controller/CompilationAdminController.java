package ru.praktikum.mainservice.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.praktikum.mainservice.compilations.model.dto.CompilationDto;
import ru.praktikum.mainservice.compilations.model.dto.NewCompilationDto;
import ru.praktikum.mainservice.compilations.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    /*
    POST COMPILATION - Добавление новой подборки
     */
    @PostMapping
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {

        log.info("Создаем новую подборку: newCompilationDto={}", newCompilationDto.toString());
        return compilationService.createCompilation(newCompilationDto);
    }

    /*
    DELETE COMPILATION - Удаление подборки
     */
    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable long compId) {

        log.info("Удаляем подборку: compId={}", compId);
        compilationService.deleteCompilation(compId);
    }

    /*
    DELETE COMPILATION - Удаление события из подборки
     */
    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable long compId,
                                           @PathVariable long eventId) {

        log.info("Удаляем событие eventId={} из подборки: compId={}", eventId, compId);
        compilationService.deleteEventFromCompilation(compId, eventId);
    }
}
