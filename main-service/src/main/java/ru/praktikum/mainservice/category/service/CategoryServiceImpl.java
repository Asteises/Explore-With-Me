package ru.praktikum.mainservice.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.praktikum.mainservice.category.mapper.CategoryMapper;
import ru.praktikum.mainservice.category.model.Category;
import ru.praktikum.mainservice.category.model.dto.CategoryDto;
import ru.praktikum.mainservice.category.model.dto.NewCategoryDto;
import ru.praktikum.mainservice.category.repository.CategoryStorage;
import ru.praktikum.mainservice.event.repository.EventStorage;
import ru.praktikum.mainservice.exception.BadRequestException;
import ru.praktikum.mainservice.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryStorage categoryStorage;
    private final EventStorage eventStorage;

    /*
    POST CATEGORIES - Добавление новой категорий
        Обратите внимание:
            имя категории должно быть уникальным;
    */
    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        // Проверяем наличие категории;
        Category category = CategoryMapper.newCategoryDtoToCategory(newCategoryDto);

        // Сохраняем и перезаписываем, чтобы записался id;
        category = categoryStorage.save(category);

        log.info("Создана новая категория: categoryId={}, name={}", category.getId(), category.getName());
        return CategoryMapper.categoryToCategoryDto(category);
    }

    /*
    PATCH CATEGORIES - Добавление новой категорий
        Обратите внимание:
            имя категории должно быть уникальным;
    */
    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        // Проверяем наличие категории;
        Category category = checkCategory(categoryDto.getId());

        // Проверяем уникальность имени категории;
        if (checkNameCategory(category.getName()) == null) {
            throw new BadRequestException(String.format("Категория с таким именем name=%s уже существует", category.getName()));
        }

        // Сетим id и name;
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());

        log.info("Внесли изменения в категорию categoryId={}, изменили имя на name={}", categoryDto.getId(), categoryDto.getName());
        return CategoryMapper.categoryToCategoryDto(category);
    }

    /*
    DELETE CATEGORIES - Добавление новой категорий
        Обратите внимание:
            с категорией не должно быть связано ни одного события;
    */
    @Override
    @Transactional
    public void deleteCategory(long catId) {
        // Проверяем наличие категории;
        Category category = checkCategory(catId);

        // Проверяем есть ли связанные с категорией события;
        eventStorage.findEventByCategory_Id(catId).orElseThrow(() -> new BadRequestException(
                "Категорию нельзя удалить, так как есть связанные с ней события"));

        log.info("Категория удалена: category={}", category.toString());
        categoryStorage.deleteById(catId);
    }

    /*
    GET CATEGORIES - Получение всех категорий;
    */
    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        // Записываем найденные в БД категории;
        List<Category> categories = categoryStorage.findAll(PageRequest.of(from / size, size)).stream().toList();

        log.info("Получаем все категории: from={}, size={}", from, size);
        return categories.stream().map(CategoryMapper::categoryToCategoryDto).collect(Collectors.toList());
    }

    /*
    GET CATEGORIES - Получение категории по id;
     */
    @Override
    public CategoryDto getCategoryById(long catId) {
        // Проверяем наличие категории;
        Category category = checkCategory(catId);

        log.info("Получаем категорию: category={}", category.toString());
        return CategoryMapper.categoryToCategoryDto(category);
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
