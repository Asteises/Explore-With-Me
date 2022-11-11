package ru.praktikum.mainservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.praktikum.mainservice.request.model.Request;

import java.util.List;

@Repository
public interface RequestStorage extends JpaRepository<Request, Long> {
    List<Request> findAllByRequester_Id(long requesterId);

    List<Request> findAllByEvent_Id(long eventId);

    List<Request> findAllByEvent_IdAndStatus(long eventId, String status);
}
