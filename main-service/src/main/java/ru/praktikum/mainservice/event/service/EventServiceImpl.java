package ru.praktikum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.StateEnum;
import ru.praktikum.mainservice.category.model.Category;
import ru.praktikum.mainservice.category.repository.CategoryStorage;
import ru.praktikum.mainservice.event.mapper.EventMapper;
import ru.praktikum.mainservice.event.model.Event;
import ru.praktikum.mainservice.event.model.EventState;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;
import ru.praktikum.mainservice.event.repository.EventStateStorage;
import ru.praktikum.mainservice.event.repository.EventStorage;
import ru.praktikum.mainservice.exception.BadRequestException;
import ru.praktikum.mainservice.exception.NotFoundException;
import ru.praktikum.mainservice.request.model.dto.UpdateEventRequest;
import ru.praktikum.mainservice.user.model.User;
import ru.praktikum.mainservice.user.repository.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventStorage eventStorage;
    private final EventStateStorage eventStateStorage;
    private final UserStorage userStorage;
    private final CategoryStorage categoryStorage;

    /*
    POST EVENT - Добавление нового события:
        Обратите внимание:
            дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента;
    */
    @Override
    public EventFullDto createEvent(long userId, NewEventDto newEventDto) {
        /*
        Из контроллера приходит только id пользователя,
        а положить в Event нужно всего пользователя, дополнительно проверяем наличие пользователя в БД;
        */
        User initiator = checkUserAvailableInDb(userId);

        /*
        В NewEventDto приходит только id категории,
        а в Event нужно будет положить всю категорию, дополнительно проверяем наличие категории в БД;
        */
        Category category = checkCategoryAvailableInDb(newEventDto.getCategory());
        Event event = EventMapper.toEvent(newEventDto);
        event.setInitiator(initiator);
        event.setCategory(category);
        // Обновляем Event, так как после сохранения в БД у него появился id;
        event = eventStorage.save(event);
        // Так как State есть только у EventFullDto, нужно его засетить;
        EventFullDto eventFullDto = EventMapper.fromEventToEventFullDto(event);
        eventFullDto.setState(StateEnum.PENDING);
        // Создаем EventState со State PENDING по умолчанию;
        EventState eventState = EventMapper.fromEventToEventState(event);
        eventStateStorage.save(eventState);
        log.info("Создан новый EventState: {}", eventState);
        log.info("Создано новое событие: {}", event);
        return eventFullDto;
    }

    /*
    PATCH EVENT - Изменение события добавленного текущим пользователем:
        Обратите внимание:
            изменить можно только отмененные события или события в состоянии ожидания модерации
            если редактируется отменённое событие, то оно автоматически переходит в состояние ожидания модерации
            дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
    */
    @Override
    public EventFullDto updateEventByCurrentUser(long userId, UpdateEventRequest updateEventRequest) {
        /*
        Из контроллера приходит только id пользователя,
        а положить в Event нужно всего пользователя, дополнительно проверяем наличие пользователя в БД;
        */
        User currentUser = checkUserAvailableInDb(userId);

        // Проверяем наличие события в БД;
        Event currentEvent = checkEventAvailableInDb(updateEventRequest.getEventId());

        // Проверяем что событие принадлежит текущему пользователю;
        if (!currentEvent.getInitiator().equals(currentUser)) {
            throw new BadRequestException(String.format("Событие eventId=%s не принадлежит данному пользователю userId=%s", currentEvent.getId(), userId));
        }

        // Проверяем чтобы событие не было опубликовано;
        EventState eventState = eventStateStorage.findEventStateByEvent_Id(currentEvent.getId());
        if (eventState.getState().equals(StateEnum.PUBLISHED.toString())) {
            throw new BadRequestException(String.format("Событие eventId=%s нельзя изменить, так как оно опубликовано", currentEvent.getId()));
        }

        // Сначала Мапим ответ, все что пришло в updateEventRequest;
        EventMapper.fromUpdateEventRequestToEvent(currentEvent, updateEventRequest);

        // Так как новые данные нужно будет сохранить в БД, то нужна вся категория, а не просто catId;
        if (updateEventRequest.getCategory() != null) {
            // Проверяем категорию;
            Category category = checkCategoryAvailableInDb(updateEventRequest.getCategory());
            currentEvent.setCategory(category);
        }

        // Обновляем данные в БД;
        eventStorage.save(currentEvent);
        EventFullDto eventFullDto = EventMapper.fromEventToEventFullDto(currentEvent);
        // Если State был CANCELED, то меняем на PENDING, сохраняем изменения для eventState;
        if (eventState.getState().equals(StateEnum.CANCELED.toString())) {
            eventState.setState(StateEnum.PENDING.toString());
            eventStateStorage.save(eventState);
        }
        // Сетим State так как он нужен EventFullDto;
        eventFullDto.setState(StateEnum.PENDING);
        log.info("Событие изменено: {}", eventFullDto);
        return eventFullDto;
    }

    /*
    GET EVENTS - Получение событий добавленных текущим пользователем:
    */
    @Override
    public List<EventFullDto> getAllOwnEvents(long userId, Integer from, Integer size) {
        User user = checkUserAvailableInDb(userId);
        List<Event> events = eventStorage.findEventByInitiator_Id(userId, PageRequest.of(from / size, size)).toList();
        log.info("Получение пользователем userId={} списка созданных событий: eventsSize={}", userId, events.size());
        return events.stream().map(EventMapper::fromEventToEventFullDto).collect(Collectors.toList());
    }

    /*
    GET EVENT - Получение полной информации о событии добавленном текущим пользователем:
    */
    @Override
    public EventFullDto getOwnEventById(long userId, long eventId) {
        User user = checkUserAvailableInDb(userId);
        Event event = checkEventAvailableInDb(eventId);
        log.info("Получение пользователем userId={} своего события: {}", userId, event);
        return EventMapper.fromEventToEventFullDto(event);
    }

    @Override
    public EventFullDto cancelOwnEvent(long userId, long eventId) {
        User user = checkUserAvailableInDb(userId);
        Event event = checkEventAvailableInDb(eventId);
        EventState eventState = c
        log.info("Отмена пользователем userId={} своего события: eventId={}", userId, eventId);
    }

    //TODO Посмотреть что можно сделать с этими методами
    private User checkUserAvailableInDb(long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Пользователь не найден в БД: userId=%s", userId)));
    }

    private Category checkCategoryAvailableInDb(long catId) {
        return categoryStorage.findById(catId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Категория не найдена: catId=%s", catId)));
    }

    @Override
    public Event checkEventAvailableInDb(long eventId) {
        return eventStorage.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Событие не найдено: eventId=%s", eventId)));
    }
}
