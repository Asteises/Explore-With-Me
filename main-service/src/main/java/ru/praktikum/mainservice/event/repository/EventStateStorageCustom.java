package ru.praktikum.mainservice.event.repository;

import org.springframework.data.domain.Pageable;
import ru.praktikum.mainservice.event.model.EventState;
import ru.praktikum.mainservice.event.model.dto.EventPublicFilterDto;

import java.util.List;

public interface EventStateStorageCustom {

    // Ищем все опубликованные события с заданными параметрами;
    List<EventState> findAllByFilterParams(EventPublicFilterDto eventPublicFilterDto, Pageable pageable);
}
