package ru.praktikum.mainservice.compilations.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.praktikum.mainservice.compilations.model.dto.CompilationDto;
import ru.praktikum.mainservice.compilations.model.dto.NewCompilationDto;

import java.util.List;

@Service
public interface CompilationService {

    List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(long compId);

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(@PathVariable long compId);

    void deleteEventFromCompilation(long compId, long eventId);
}
