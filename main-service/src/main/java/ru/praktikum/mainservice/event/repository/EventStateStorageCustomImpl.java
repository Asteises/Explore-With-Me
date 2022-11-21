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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventStateStorageCustomImpl implements EventStateStorageCustom {

    // Для корректного открытия и закрытия EntityManager;
    @PersistenceContext
    private EntityManager entityManager;

    // Ищем только опубликованные события: State - PUBLISHED
    @Override
    public List<EventState> findAllByFilterParams(EventPublicFilterDto eventPublicFilterDto, Pageable pageable) {
        return null;
    }

    private List<Predicate> getPredicates(EventPublicFilterDto eventDto,
                                          CriteriaBuilder cb,
                                          Root<EventState> eventState) {

        List<Predicate> predicates = new ArrayList<>();

        if (eventDto.getState() != null) {
            Predicate statePredicate = cb.equal(eventState.get("state"), eventDto.getState());
            predicates.add(statePredicate);
        }

        if (eventDto.getText() != null) {
            Predicate textAnnotationPredicate = cb.equal(eventState.get("event").get("annotation"), eventDto.getText());
            predicates.add(textAnnotationPredicate);

            Predicate textDescriptionPredicate = cb.equal(eventState.get("event").get("description"), eventDto.getText());
            predicates.add(textDescriptionPredicate);
        }

        if(eventDto.getCategories() != null) {
            List<Long> catIds = Arrays.asList(eventDto.getCategories());

            Expression<Long> catEventIds = eventState.get("event").get("category").get("id");

            Predicate predicateCatIn = catEventIds.in(catIds);
            predicates.add(predicateCatIn);
        }

        if(eventDto.getPaid() != null) {
            Predicate paidPredicate = cb.equal(eventState.get("event").get("paid"), eventDto.getPaid());
            predicates.add(paidPredicate);
        }

        if(eventDto.getRangeStart() != null && eventDto.getRangeEnd() != null) {
            Predicate rangeStartEndPredicate = cb.between(eventState.get("createdOn"),
                    eventDto.getRangeStart(),
                    eventDto.getRangeEnd());
            predicates.add(rangeStartEndPredicate);
        }

        return predicates;
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
