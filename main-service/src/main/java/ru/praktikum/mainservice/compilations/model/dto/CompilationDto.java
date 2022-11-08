package ru.praktikum.mainservice.compilations.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.praktikum.mainservice.event.model.dto.EventShortDto;

import javax.validation.Valid;
import java.util.List;

/**
 * Подборка событий
 */

@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    @JsonProperty("events")
    @Valid
    private List<EventShortDto> events = null;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("pinned")
    private Boolean pinned = null;

    @JsonProperty("title")
    private String title = null;
}
