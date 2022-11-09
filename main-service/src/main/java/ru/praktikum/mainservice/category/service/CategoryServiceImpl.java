package ru.praktikum.mainservice.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.praktikum.mainservice.category.mapper.CategoryMapper;
import ru.praktikum.mainservice.category.model.Category;
import ru.praktikum.mainservice.category.model.dto.CategoryDto;
import ru.praktikum.mainservice.category.model.dto.NewCategoryDto;
import ru.praktikum.mainservice.category.repository.CategoryStorage;
import ru.praktikum.mainservice.exception.BadRequestException;
import ru.praktikum.mainservice.exception.NotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryStorage categoryStorage;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.newCategoryDtoToCategory(newCategoryDto);
        category = categoryStorage.save(category);
        log.info("Создана новая категория: categoryId={}, name={}", category.getId(), category.getName());
        return CategoryMapper.categoryToCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = checkCategory(categoryDto.getId());
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        if (checkNameCategory(category.getName()) == null) {
            throw new BadRequestException(String.format("Категория с таким именем name=%s уже существует", category.getName()));
        }
        log.info("Внесли изменения в категорию categoryId={}, изменили имя на name={}", categoryDto.getId(), categoryDto.getName());
        return CategoryMapper.categoryToCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(long catId) {
        checkCategory(catId);
        log.info("Категория удалена: catId={}", catId);
        categoryStorage.deleteById(catId);
    }

    public Category checkCategory(long catId) {
        log.info("Проверяем существование категории catId={}", catId);
        return categoryStorage.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория не найдена: categoryId=%s", catId)));
    }

    public Boolean checkNameCategory(String catName) {
        log.info("Проверяем существование категории catName={}", catName);
        return categoryStorage.findCategoryByName(catName) != null;
    }
}
