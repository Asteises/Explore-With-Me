package ru.praktikum.statisticservice;

import org.springframework.stereotype.Service;
import ru.praktikum.statisticservice.model.dto.EndpointHitDto;

@Service
public interface StatService {

    void save(EndpointHitDto endpointHitDto);

    Integer getEventStatInfo(long eventId);
}
