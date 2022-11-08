package ru.praktikum.mainservice.request.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Данные для изменения информации о событии
 */
@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    @JsonProperty("annotation")
    private String annotation = null;

    @JsonProperty("category")
    private Long category = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("eventDate")
    private String eventDate = null;

    @JsonProperty("eventId")
    private Long eventId = null;

    @JsonProperty("paid")
    private Boolean paid = null;

    @JsonProperty("participantLimit")
    private Integer participantLimit = null;

    @JsonProperty("title")
    private String title = null;
}
