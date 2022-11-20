package ru.praktikum.mainservice.event.repository;

import org.springframework.data.domain.Pageable;
import ru.praktikum.mainservice.event.model.Event;
import ru.praktikum.mainservice.event.model.EventState;
import ru.praktikum.mainservice.event.model.dto.EventFullDto;
import ru.praktikum.mainservice.event.model.dto.EventPublicFilterDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class EventStateStorageCustomImpl implements EventStateStorageCustom {

    @PersistenceContext // Для корректного открытия и закрытия EntityManager;
    private EntityManager entityManager;

    // Ищем только опубликованные события: State - PUBLISHED
    @Override
    public List<EventState> findAllByFilterParams(EventPublicFilterDto eventPublicFilterDto, Pageable pageable) {
        return null;
    }

    private List<Predicate> getPredicates() {
        return null;
    }

    public List<EventFullDto> searchEventsByPredicates(Long[] users,
                                                       String[] states,
                                                       Long[] categories,
                                                       String rangeStart,
                                                       String rangeEnd,
                                                       Integer from,
                                                       Integer size) {

        // Создаем менеджер для работы с предикатами;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Какой объект мы хотим получить в результате запроса;
        CriteriaQuery<Event> query = cb.createQuery(Event.class);

        // Как RawMApper, позволяет работать с объектами выбранного класса;
        Root<Event> order = query.from(Event.class);

        // Создаем список куда будем складывать предикаты;
//        List<Predicate> predicates = getPredicates(orderFilterDto, cb, order);

        return null;
    }
}
