package ru.praktikum.mainservice.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.compilations.mapper.CompilationMapper;
import ru.praktikum.mainservice.compilations.model.Compilation;
import ru.praktikum.mainservice.compilations.model.dto.CompilationDto;
import ru.praktikum.mainservice.compilations.repository.CompilationStorage;
import ru.praktikum.mainservice.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationStorage compilationStorage;

    /*
    GET COMPILATION - Получение подборок событий
    */
    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {

        // Собираем лист всех подборок по заданным параметрам;
        List<Compilation> compilations = compilationStorage
                .findAllByPinned(pinned, PageRequest.of(from / size, size))
                .stream()
                .toList();

        log.info("Получаем все подборки pinned={}, from={}, size={}", pinned, from, size);
        return compilations
                .stream()
                .map(CompilationMapper::fromCompToCompDto)
                .collect(Collectors.toList());
    }

    /*
    GET COMPILATION - Получение подборки по id
    */
    @Override
    public CompilationDto getCompilationById(long compId) {

        // Проверяем существует подборка или нет;
        Compilation compilation = checkCompilationAvailableInBd(compId);

        log.info("Получаем подборку compId={}", compId);
        return CompilationMapper.fromCompToCompDto(compilation);
    }

    /*
    Метод для проверки наличия подборки в БД
     */
    public Compilation checkCompilationAvailableInBd(long compId) {
        return compilationStorage.findById(compId).orElseThrow(() -> new NotFoundException(String
                .format("Подборка не найдена: compId=%s", compId)));
    }


}
