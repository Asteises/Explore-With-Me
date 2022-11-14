package ru.praktikum.mainservice.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.compilations.mapper.CompilationMapper;
import ru.praktikum.mainservice.compilations.model.Compilation;
import ru.praktikum.mainservice.compilations.model.CompilationEvent;
import ru.praktikum.mainservice.compilations.model.dto.CompilationDto;
import ru.praktikum.mainservice.compilations.model.dto.NewCompilationDto;
import ru.praktikum.mainservice.compilations.repository.CompilationEventStorage;
import ru.praktikum.mainservice.compilations.repository.CompilationStorage;
import ru.praktikum.mainservice.event.mapper.EventMapper;
import ru.praktikum.mainservice.event.model.Event;
import ru.praktikum.mainservice.event.model.dto.EventShortDto;
import ru.praktikum.mainservice.event.service.EventService;
import ru.praktikum.mainservice.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationStorage compilationStorage;

    private final CompilationEventStorage compilationEventStorage;

    private final EventService eventService;

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

        // Создаем результирующий объект;
        List<CompilationDto> result = new ArrayList<>();

        // Для каждой подборки создаем CompilationDto;
        for (Compilation compilation : compilations) {

            // Используем метод getCompilationById;
            CompilationDto compilationDto = getCompilationById(compilation.getId());

            // Записываем в результирующий список;
            result.add(compilationDto);
        }

        log.info("Получаем все подборки pinned={}, from={}, size={}", pinned, from, size);
        return result;
    }

    /*
    GET COMPILATION - Получение подборки по id
    */
    @Override
    public CompilationDto getCompilationById(long compId) {

        // Проверяем существует подборка или нет;
        Compilation compilation = checkCompilationAvailableInBd(compId);

        // Находим все CompilationEvent связанные с этой подборкой;
        List<CompilationEvent> compilationEvents = compilationEventStorage.findAllByComp(compilation);

        // Находим все события, связанные с этой подборкой;
        List<Event> events = new ArrayList<>();
        for (CompilationEvent compilationEvent : compilationEvents) {
            events.add(compilationEvent.getEvent());
        }

        // Мапим CompilationDto;
        CompilationDto result = CompilationMapper.fromCompToCompDto(compilation);

        // Сетим подборки;
        result.setEvents(events.stream()
                .map(EventMapper::fromEventToEventShortDto)
                .collect(Collectors.toList()));

        log.info("Получаем подборку compId={}", compId);
        return result;
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

        // Находим все события по пришедшим id;
        List<Event> events = eventService.getEventsByIds(newCompilationDto.getEvents());

        // Сохраняем подборки вместе с событиями в CompilationEvent;
        //TODO Не пойму правильно ли я сохраняю события в подборках?
        for (Event event : events) {

            // Проверяем каждое событие на существование;
            event = eventService.checkEventAvailableInDb(event.getId());

            // Создаем CompilationEvent, мапим и сохраняем в БД;
            CompilationEvent compilationEvent = CompilationMapper.toCompilationEvent(compilation, event);
            compilationEventStorage.save(compilationEvent);
        }

        // Мапим события в лист EventShortDto;
        List<EventShortDto> eventShortDtos = events.stream()
                .map(EventMapper::fromEventToEventShortDto).toList();

        // Сетим события в результат;
        result.setEvents(eventShortDtos);

        log.info("Подборка успешно создана result={}", result);
        return result;
    }

    /*
    DELETE COMPILATION - Удаление подборки
     */
    @Override
    public void deleteCompilation(long compId) {

        // Проверяем существование подборки;
        Compilation compilation = checkCompilationAvailableInBd(compId);

        // Проверяем существование CompilationEvent;
        CompilationEvent compilationEvent = checkCompilationEventByComp(compilation);

        // Удаляем CompilationEvent;
        compilationEventStorage.delete(compilationEvent);

        log.info("Удаляем подборку compId={}", compId);
        compilationStorage.delete(compilation);
    }

    /*
    DELETE COMPILATION - Удаление события из подборки
    */
    @Override
    public void deleteEventFromCompilation(long compId, long eventId) {

        // Проверяем существование подборки;
        Compilation compilation = checkCompilationAvailableInBd(compId);

        // Проверяем существование события;
        Event event = eventService.checkEventAvailableInDb(eventId);

        // И удаляем его из БД;
        compilationEventStorage.deleteCompilationEventByEventAndComp(event, compilation);
    }


    /*
    Метод для проверки наличия подборки в БД
    */
    private Compilation checkCompilationAvailableInBd(long compId) {

        log.info("Проверяем существование подборки compId={}", compId);
        return compilationStorage.findById(compId).orElseThrow(() -> new NotFoundException(
                String.format("Подборка не найдена: compId=%s", compId)));
    }

    /*
    Метод для проверки наличия CompilationEvent в БД по подборке;
    */
    private CompilationEvent checkCompilationEventByComp(Compilation compilation) {

        log.info("Проверяем существование CompilationEvent с параметрами: compilation={}", compilation);
        return compilationEventStorage.findCompilationEventByComp(compilation).orElseThrow(() -> new NotFoundException(
                String.format("CompilationEvent не найден, параметры: compilation=%s", compilation)));
    }

}
