package ru.praktikum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.event.enums.StateEnum;
import ru.praktikum.mainservice.category.model.Category;
import ru.praktikum.mainservice.category.repository.CategoryStorage;
import ru.praktikum.mainservice.event.mapper.EventMapper;
import ru.praktikum.mainservice.event.model.Event;
import ru.praktikum.mainservice.event.model.EventState;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.EventShortDto;
import ru.praktikum.mainservice.event.model.dto.NewEventDto;
import ru.praktikum.mainservice.event.repository.EventStateStorage;
import ru.praktikum.mainservice.event.repository.EventStorage;
import ru.praktikum.mainservice.exception.BadRequestException;
import ru.praktikum.mainservice.exception.NotFoundException;
import ru.praktikum.mainservice.request.mapper.RequestMapper;
import ru.praktikum.mainservice.request.model.Request;
import ru.praktikum.mainservice.request.model.dto.ParticipationRequestDto;
import ru.praktikum.mainservice.request.model.dto.UpdateEventRequest;
import ru.praktikum.mainservice.request.repository.RequestStorage;
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
    private final RequestStorage requestStorage;

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

        // Мапим событие и сетим пользователя и категорию;
        Event event = EventMapper.toEvent(newEventDto);
        event.setInitiator(initiator);
        event.setCategory(category);

        // Обновляем Event, так как после сохранения в БД у него появился id;
        event = eventStorage.save(event);

        // Так как State есть у EventFullDto, нужно его засетить;
        EventFullDto eventFullDto = EventMapper.fromEventToEventFullDto(event);
        eventFullDto.setState(StateEnum.PENDING);

        // Создаем EventState со State PENDING по умолчанию;
        EventState eventState = EventMapper.fromEventToEventState(event);
        // И сохраняем в БД с новым значением;
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

        // Находим EventState события, чтобы проверить статус;
        EventState eventState = checkEventStateAvailableInDb(currentEvent.getId());
        // Проверяем чтобы событие не было опубликовано;
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
    public List<EventFullDto> getAllEventsByCurrentUser(long userId, Integer from, Integer size) {
        // Проверяем, что пользователь существует;
        User user = checkUserAvailableInDb(userId);

        // Собираем все события принадлежащие пользователю;
        List<Event> events = eventStorage.findEventByInitiator_Id(userId, PageRequest.of(from / size, size)).toList();

        log.info("Получение пользователем userId={} списка созданных событий: eventsSize={}", userId, events.size());
        return events.stream().map(EventMapper::fromEventToEventFullDto).collect(Collectors.toList());
    }

    /*
    GET EVENT - Получение полной информации о событии добавленном текущим пользователем:
    */
    @Override
    public EventFullDto getEventByIdByCurrentUser(long userId, long eventId) {
        // Проверяем, что пользователь существует;
        User user = checkUserAvailableInDb(userId);

        // Проверяем, что событие существует;
        Event event = checkEventAvailableInDb(eventId);

        // Проверяем что событие принадлежит текущему пользователю;
        checkOwnEvent(event, user);

        log.info("Получение пользователем userId={} своего события: {}", userId, event);
        return EventMapper.fromEventToEventFullDto(event);
    }

    /*
    PATCH EVENT - Отмена события добавленного текущим пользователем:
        Обратите внимание:
            Отменить можно только событие в состоянии ожидания модерации;
     */
    @Override
    public EventFullDto cancelEventByCurrentUser(long userId, long eventId) {
        // Проверяем, что пользователь существует;
        User user = checkUserAvailableInDb(userId);

        // Проверяем, что событие существует;
        Event event = checkEventAvailableInDb(eventId);

        // Проверяем что событие принадлежит текущему пользователю;
        checkOwnEvent(event, user);

        // Находим EventState события, чтобы проверить статус;
        EventState eventState = checkEventStateAvailableInDb(eventId);

        // Проверяем статус;
        checkStatePending(eventState);

        // Сетим статус отмены в eventState и сохраняем в БД;
        eventState.setState(StateEnum.CANCELED.toString());
        eventStateStorage.save(eventState);

        // Мапим EventFullDto из event;
        EventFullDto result = EventMapper.fromEventToEventFullDto(event);

        // Сетим статус отмены;
        result.setState(StateEnum.CANCELED);
        log.info("Отмена пользователем userId={} своего события: result={}", userId, result);
        return result;
    }

    /*
    GET EVENT - Получение информации о запросах на участие в событии текущего пользователя:
    */
    @Override
    public List<ParticipationRequestDto> getRequestsByEventByCurrentUser(long userId, long eventId) {
        // Проверяем, что пользователь существует;
        User user = checkUserAvailableInDb(userId);

        // Проверяем, что событие существует;
        Event event = checkEventAvailableInDb(eventId);

        // Проверяем что событие принадлежит текущему пользователю;
        checkOwnEvent(event, user);

        // Находим все реквесты на данное событие;
        List<Request> requests = requestStorage.findAllByEvent_Id(eventId);

        // Мапим все найденные запросы в лист ParticipationRequestDto;
        log.info("Получили все запросы на событие: eventId={} созданного пользователем userId={}: requests {}",
                event.getId(), user.getId(), requests.toString());
        return requests.stream()
                .map(RequestMapper::fromRequestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    /*
    PATCH EVENT - Подтверждение чужой заявки на участие в событии текущего пользователя:
        Обратите внимание:
            если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется;
            нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие;
            если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить;
     */
    @Override
    public ParticipationRequestDto acceptRequestOnEventByCurrentUser(long userId, long eventId, long reqId) {
        // Проверяем, что пользователь существует;
        User user = checkUserAvailableInDb(userId);

        // Проверяем, что событие существует;
        Event event = checkEventAvailableInDb(eventId);

        // Проверяем что событие принадлежит текущему пользователю;
        checkOwnEvent(event, user);

        // Проверяем что запрос существует;
        Request request = checkRequestAvailableInDb(reqId);

        ParticipationRequestDto pRDto = RequestMapper.fromRequestToParticipationRequestDto(request);

        // Проверяем лимит заявок для участия в событии и модерацию;
        Boolean checkResult = checkRequestLimitAndModeration(event);

        // Если нет лимита и отключена модерация;
        if (checkResult) {
            // Cетим статус и сохраняем обновленные данные в БД;
            request.setStatus("CONFIRMED");
            requestStorage.save(request);

            // Если данный запрос стал последним из одобренных;
        } else {
            // Cетим статус и сохраняем обновленные данные в БД;
            request.setStatus("CONFIRMED");
            requestStorage.save(request);

            // А остальные не одобренные запросы отклоняем;
            List<Request> requests = requestStorage.findAllByEvent_IdAndStatus(eventId, "PENDING");
            requests.forEach(req -> req.setStatus("CANCELED"));

            // Если лимит исчерпан, выбрасываем исключение;
        }

        pRDto.setStatus("CONFIRMED");
        log.info("Пользователь userId={} принял запрос reqId={} на событие: eventId={}",
                userId, reqId, eventId);
        return pRDto;
    }

    /*
   PATCH EVENT - Отклонение чужой заявки на участие в событии текущего пользователя:
    */
    @Override
    public ParticipationRequestDto cancelRequestOnEventByCurrentUser(long userId, long eventId, long reqId) {
        // Проверяем, что пользователь существует;
        User user = checkUserAvailableInDb(userId);

        // Проверяем, что событие существует;
        Event event = checkEventAvailableInDb(eventId);

        // Проверяем что событие принадлежит текущему пользователю;
        checkOwnEvent(event, user);

        // Проверяем что запрос существует;
        Request request = checkRequestAvailableInDb(reqId);

        // Сетим новый статус;
        request.setStatus("REJECTED");
        requestStorage.save(request);

        log.info("Пользователь userId={} отклонил запрос reqId={} на событие: eventId={}",
                userId, reqId, eventId);
        return RequestMapper.fromRequestToParticipationRequestDto(request);
    }

    /*
    GET EVENTS - Получение событий с возможностью фильтрации
        Обратите внимание:
            это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события;
            текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв;
            если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени;
            информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие;
            информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики;
     */
    @Override
    public List<EventShortDto> getAllPublicEvents(String text,
                                                  int[] categories,
                                                  Boolean paid,
                                                  String rangeStart,
                                                  String rangeEnd,
                                                  Boolean onlyAvailable,
                                                  String sort,
                                                  Integer from,
                                                  Integer size) {
        return null;
    }

    /*
    Получение подробной информации об опубликованном событии по его идентификатору
        Обратите внимание:
            событие должно быть опубликовано;
            информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов;
            информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики;
     */
    @Override
    public EventFullDto getPublicEventById(long id) {
        return null;
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
    public EventState checkEventStateAvailableInDb(long eventId) {
        return eventStateStorage.findEventStateByEvent_Id(eventId).orElseThrow(() -> new NotFoundException(String
                .format("EventState не найден: eventId=%s", eventId)));
    }

    private Request checkRequestAvailableInDb(long reqId) {
        return requestStorage.findById(reqId).orElseThrow(() -> new NotFoundException(String
                .format("Запрос не найден: reqId=%s", reqId)));
    }

    @Override
    public Event checkEventAvailableInDb(long eventId) {
        return eventStorage.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Событие не найдено: eventId=%s", eventId)));
    }

    /*
    Метод для проверки инициатора события, что событие принадлежит именно этому пользователю;
     */
    private void checkOwnEvent(Event event, User user) {
        if (!event.getInitiator().equals(user)) {
            throw new BadRequestException(String
                    .format("Пользователю userId=%s не принадлежит данное событие eventId=%s", user.getId(), event.getId()));
        }
        log.info("Проверяем инициатора userId={} своего события: eventId={}", user, event);
    }

    /*
    Метод проверяет статус у EventState;
     */
    private void checkStatePending(EventState eventState) {
        if (!eventState.getState().equals(StateEnum.PENDING.toString())) {
            throw new BadRequestException(String
                    .format("Событие имеет статус отличный от модерации state=%s", eventState.getState()));
        }
        log.info("Проверяем статус у eventStateId={} : state={}", eventState.getId(), eventState.getState());
    }

    /*
    Метод проверяет количество одобренных заявок;
    */
    @Override
    public Boolean checkRequestLimitAndModeration(Event event) {

        long totalLimit = event.getParticipantLimit();

        // Находим количество всех одобренных заявок;
        long currentLimit = requestStorage.findAllByEvent_IdAndStatus(event.getId(), "CONFIRMED").size();

        // Если нет лимита и отключена модерация;
        if (totalLimit == 0
                && event.getRequestModeration().equals(Boolean.FALSE)) {
            return true;
            // Если осталось последнее место;
        } else if (totalLimit == currentLimit + 1) {
            return false;
            // Если лимит исчерпан;
        } else  {
            throw new BadRequestException(String.format("Лимит заявок на событие превышен: eventId={}", event.getId()));
        }
    }

}
