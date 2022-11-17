package ru.praktikum.statisticservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.praktikum.statisticservice.model.EndpointHit;

@Repository
public interface StatStorage extends JpaRepository<EndpointHit, Long> {

    @Query(value = "select count (eh) from endpoint_hit eh where eh.uri = concat('/events/', :eventId)", nativeQuery = true)
    Integer getViewsEventById(long eventId);
}
