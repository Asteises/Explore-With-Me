package ru.praktikum.mainservice.user.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Пользователь
 */

@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("email")
    @NotNull
    @NotBlank
    private String email;

    @JsonProperty("name")
    @NotNull
    @NotBlank
    private String name;
}
