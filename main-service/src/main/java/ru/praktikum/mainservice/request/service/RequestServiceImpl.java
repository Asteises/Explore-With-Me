package ru.praktikum.mainservice.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.praktikum.mainservice.StateEnum;
import ru.praktikum.mainservice.event.model.Event;
import ru.praktikum.mainservice.event.service.EventService;
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

    @Override
    public ParticipationRequestDto createRequest(long userId, long eventId) {
        Event event = eventService.checkEventAvailableInDb(eventId);
        User currentUser = userService.checkUserAvailableInDb(userId);
        Request request = new Request();
        request.setRequester(currentUser);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        request.setStatus(StateEnum.PENDING.toString());
        request = requestStorage.save(request);

        // TODO Проблема с тестом - eventId = 0
        // TODO Проставить логи
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
}
