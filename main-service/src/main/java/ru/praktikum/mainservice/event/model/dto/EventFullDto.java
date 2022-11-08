package ru.praktikum.mainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.praktikum.mainservice.category.model.dto.CategoryDto;
import ru.praktikum.mainservice.Location;
import ru.praktikum.mainservice.StateEnum;
import ru.praktikum.mainservice.user.model.dto.UserShortDto;

/**
 * EventFullDto
 */
@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    @JsonProperty("annotation")
    private String annotation = null;

    @JsonProperty("category")
    private CategoryDto category = null;

    @JsonProperty("confirmedRequests")
    private Long confirmedRequests = null;

    @JsonProperty("createdOn")
    private String createdOn = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("eventDate")
    private String eventDate = null;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("initiator")
    private UserShortDto initiator = null;

    @JsonProperty("location")
    private Location location = null;

    @JsonProperty("paid")
    private Boolean paid = null;

    @JsonProperty("participantLimit")
    private Integer participantLimit = 0;

    @JsonProperty("publishedOn")
    private String publishedOn = null;

    @JsonProperty("requestModeration")
    private Boolean requestModeration = true;

    @JsonProperty("state")
    private StateEnum state = null;

    @JsonProperty("title")
    private String title = null;

    @JsonProperty("views")
    private Long views = null;
}
