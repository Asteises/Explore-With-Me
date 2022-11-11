package ru.praktikum.mainservice.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.praktikum.mainservice.compilations.model.dto.CompilationDto;
import ru.praktikum.mainservice.compilations.service.CompilationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationController {

    private final CompilationService compilationService;

    /*
    GET COMPILATION - Получение подборок событий
    */
    @GetMapping()
    public List<CompilationDto> getAllCompilations(@RequestParam Boolean pinned,
                                                   @RequestParam Integer from,
                                                   @RequestParam Integer size) {
        log.info("Получаем все подборки pinned={}, from={}, size={}", pinned, from, size);
        return compilationService.getAllCompilations(pinned, from, size);
    }

    /*
    GET COMPILATION - Получение подборки по id
    */
    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        log.info("Получаем подборку compId={}", compId);
        return compilationService.getCompilationById(compId);
    }
}
