package ru.praktikum.mainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.praktikum.mainservice.Location;

/**
 * Новое событие
 */

@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
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
    private Boolean paid = false;

    @JsonProperty("participantLimit")
    private Integer participantLimit = 0;

    @JsonProperty("requestModeration")
    private Boolean requestModeration = true;

    @JsonProperty("title")
    private String title = null;
}
