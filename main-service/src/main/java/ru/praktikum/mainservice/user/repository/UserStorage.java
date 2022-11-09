package ru.praktikum.mainservice.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.praktikum.mainservice.user.model.User;

import java.util.List;

@Repository
public interface UserStorage extends JpaRepository<User, Long> {

    Page<User> findUsersByIdIn(List<Long> ids, Pageable pageable);
}
