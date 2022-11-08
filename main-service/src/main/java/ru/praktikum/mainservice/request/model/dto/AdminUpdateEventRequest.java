package ru.praktikum.mainservice.request.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.praktikum.mainservice.Location;

/**
 * Информация для редактирования события администратором. Все поля необязательные. Значение полей не валидируется.
 */
@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateEventRequest {
    @JsonProperty("annotation")
    private String annotation = null;

    @JsonProperty("category")
    private Long category = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("eventDate")
    private String eventDate = null;

    @JsonProperty("location")
    private Location location = null;

    @JsonProperty("paid")
    private Boolean paid = null;

    @JsonProperty("participantLimit")
    private Integer participantLimit = null;

    @JsonProperty("requestModeration")
    private Boolean requestModeration = null;

    @JsonProperty("title")
    private String title = null;
}
