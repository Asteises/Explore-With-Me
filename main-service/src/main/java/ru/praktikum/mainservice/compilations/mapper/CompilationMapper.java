package ru.praktikum.mainservice.compilations.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.compilations.model.Compilation;
import ru.praktikum.mainservice.compilations.model.dto.CompilationDto;

import java.util.ArrayList;

@Slf4j
@Service
public class CompilationMapper {

    public static CompilationDto fromCompToCompDto(Compilation compilation) {
        CompilationDto compDto = new CompilationDto();
        compDto.setEvents(new ArrayList<>());
        compDto.setId(compilation.getId());
        compDto.setPinned(compilation.getPinned());
        compDto.setTitle(compilation.getTitle());

        return compDto;
    }
}
