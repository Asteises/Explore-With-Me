package ru.praktikum.mainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.praktikum.mainservice.category.model.dto.CategoryDto;
import ru.praktikum.mainservice.user.model.dto.UserShortDto;

/**
 * Краткая информация о событии
 */

@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    @JsonProperty("annotation")
    private String annotation = null;

    @JsonProperty("category")
    private CategoryDto category = null;

    @JsonProperty("confirmedRequests")
    private Long confirmedRequests = null;

    @JsonProperty("eventDate")
    private String eventDate = null;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("initiator")
    private UserShortDto initiator = null;

    @JsonProperty("paid")
    private Boolean paid = null;

    @JsonProperty("title")
    private String title = null;

    @JsonProperty("views")
    private Long views = null;
}
