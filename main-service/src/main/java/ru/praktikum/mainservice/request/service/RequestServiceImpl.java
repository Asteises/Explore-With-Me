package ru.praktikum.mainservice.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.event.enums.StateEnum;
import ru.praktikum.mainservice.event.model.Event;
import ru.praktikum.mainservice.event.model.EventState;
import ru.praktikum.mainservice.event.service.EventService;
import ru.praktikum.mainservice.exception.BadRequestException;
import ru.praktikum.mainservice.exception.NotFoundException;
import ru.praktikum.mainservice.request.mapper.RequestMapper;
import ru.praktikum.mainservice.request.model.Request;
import ru.praktikum.mainservice.request.model.dto.ParticipationRequestDto;
import ru.praktikum.mainservice.request.repository.RequestStorage;
import ru.praktikum.mainservice.user.model.User;
import ru.praktikum.mainservice.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestStorage requestStorage;
    private final EventService eventService;
    private final UserService userService;

    /*
    POST REQUEST - Добавление запроса от текущего пользователя на участие в событии
        Обратите внимание:
            + нельзя добавить повторный запрос;
            + инициатор события не может добавить запрос на участие в своём событии;
            + нельзя участвовать в неопубликованном событии;
            + если у события достигнут лимит запросов на участие - необходимо вернуть ошибку;
            + если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного;
    */
    @Override
    public ParticipationRequestDto createRequest(long userId, long eventId) {
        // Проверяем наличие события;
        Event event = eventService.checkEventAvailableInDb(eventId);

        // Проверяем опубликовано событие или нет;
        checkEventStatusNotPublished(event.getId());

        // Проверяем есть ли доступные места на событие;
        eventService.checkRequestLimitAndModeration(event);

        // Проверяем наличие пользователя;
        User currentUser = userService.checkUserAvailableInDb(userId);

        // Проверяем что текущий пользователь не является инициатором события;
        checkRequesterNotInitiator(event, currentUser);

        // Проверяем что запроса на это событие еще нет;
        checkRepeatRequest(eventId, userId);

        // Создаем новый запрос и сетим данные;
        Request request = new Request();
        request.setRequester(currentUser);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());

        // Проверяем есть ли у события пре-модерация;
        if (event.getRequestModeration()) {
            request.setStatus("CONFIRMED");
        } else {
            request.setStatus("PENDING");
        }

        // Сохраняем запрос в БД и обновляем, чтобы записать id;
        request = requestStorage.save(request);

        // TODO Проблема с тестом в Postman - eventId = 0
        log.info("Пользователь userId={} создает новый запрос а событие eventId={}", userId, eventId);
        return RequestMapper.fromRequestToParticipationRequestDto(request);
    }

    @Override
    public ParticipationRequestDto cancelOwnRequest(long userId, long requestId) {
        Request request = checkRequestAvailableInDb(requestId);
        request.setStatus(StateEnum.CANCELED.toString());
        requestStorage.save(request);
        return RequestMapper.fromRequestToParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(long userId) {
        User user = userService.checkUserAvailableInDb(userId);
        List<Request> requests = requestStorage.findAllByRequester_Id(userId);
        return requests.stream().map(RequestMapper::fromRequestToParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public Request checkRequestAvailableInDb(long requestId) {
        return requestStorage.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с таким requestId=%s не найден", requestId)));
    }

    private void checkRepeatRequest(long eventId, long requesterId) {
        requestStorage.findRequestByEvent_IdAndRequester_Id(eventId, requesterId)
                .orElseThrow(() -> new BadRequestException(String.format("Повторный запрос от пользователя " +
                        "requesterId=%s на событие eventId=%s", requesterId, eventId)));
    }

    private void checkRequesterNotInitiator(Event event, User requester) {
        if (event.getInitiator().equals(requester)) {
            throw new BadRequestException(String.format("Запрос не может быть создан инициатором " +
                    "события requesterId={}", requester.getId()));
        }
    }

    private void checkEventStatusNotPublished(long eventId) {
        EventState eventState = eventService.checkEventStateAvailableInDb(eventId);
        if(!eventState.getState().equals("PUBLISHED")) {
            throw new BadRequestException(String.format("Запрос не может быть создан на неопубликованное " +
                    "событие eventId={}", eventId));
        }
    }
}
