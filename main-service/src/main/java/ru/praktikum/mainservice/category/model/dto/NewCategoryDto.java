package ru.praktikum.mainservice.category.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Данные для добавления новой категории
 */

@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    @JsonProperty("name")
    private String name = null;
}
