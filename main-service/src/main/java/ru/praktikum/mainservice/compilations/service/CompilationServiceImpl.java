package ru.praktikum.mainservice.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.compilations.mapper.CompilationMapper;
import ru.praktikum.mainservice.compilations.model.Compilation;
import ru.praktikum.mainservice.compilations.model.dto.CompilationDto;
import ru.praktikum.mainservice.compilations.model.dto.NewCompilationDto;
import ru.praktikum.mainservice.compilations.repository.CompilationStorage;
import ru.praktikum.mainservice.event.mapper.EventMapper;
import ru.praktikum.mainservice.event.model.dto.EventShortDto;
import ru.praktikum.mainservice.event.repository.EventStorage;
import ru.praktikum.mainservice.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationStorage compilationStorage;

    private final EventStorage eventStorage;

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
    POST COMPILATION - Добавление новой подборки
    */
    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {

        // Создаем новую категорию и сохраняем в БД;
        Compilation compilation = CompilationMapper.fromNewCompToCom(newCompilationDto);
        compilationStorage.save(compilation);

        // Мапим результирующий объект;
        CompilationDto result = CompilationMapper.fromCompToCompDto(compilation);

        //TODO Нужно ли делать проверку каждого пришедшего события на существование?

        // Находим все события по пришедшим id, и мапим их в лист EventShortDto;
        List<EventShortDto> events = eventStorage.findAllById(newCompilationDto.getEvents()).stream()
                .map(EventMapper::fromEventToEventShortDto).toList();

        // Сетим события в результат;
        result.setEvents(events);

        return result;
    }

    /*
    Метод для проверки наличия подборки в БД
    */
    private Compilation checkCompilationAvailableInBd(long compId) {

        log.info("Проверяем существование подборки compId={}", compId);
        return compilationStorage.findById(compId).orElseThrow(() -> new NotFoundException(
                String.format("Подборка не найдена: compId=%s", compId)));
    }

}
