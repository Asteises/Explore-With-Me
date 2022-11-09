package ru.praktikum.mainservice.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.praktikum.mainservice.category.model.dto.CategoryDto;
import ru.praktikum.mainservice.category.model.dto.NewCategoryDto;
import ru.praktikum.mainservice.category.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Создаем новую категорию {}", newCategoryDto.toString());
        return categoryService.createCategory(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Меняем категорию: categoryId={}, name={}", categoryDto.getId(), categoryDto.getName());
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteUser(@PathVariable long catId) {
        log.info("Удаляем категорию catId={}", catId);
        categoryService.deleteCategory(catId);
    }
}
