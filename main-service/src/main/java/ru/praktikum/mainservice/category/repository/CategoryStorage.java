package ru.praktikum.mainservice.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.praktikum.mainservice.category.model.Category;

@Repository
public interface CategoryStorage extends JpaRepository<Category, Long> {

    Category findCategoryByName(String name);
}
