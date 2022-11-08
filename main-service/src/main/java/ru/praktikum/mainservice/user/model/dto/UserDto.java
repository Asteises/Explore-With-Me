package ru.praktikum.mainservice.user.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Пользователь
 */

@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @JsonProperty("email")
    private String email = null;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;
}
