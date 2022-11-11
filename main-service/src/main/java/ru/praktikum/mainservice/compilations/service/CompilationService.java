package ru.praktikum.mainservice.compilations.service;

import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.compilations.model.dto.CompilationDto;

import java.util.List;

@Service
public interface CompilationService {

    List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(long compId);
}
