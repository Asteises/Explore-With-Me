package ru.praktikum.statisticservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

/**
 * ViewStats
 */
@Validated
public class ViewStats {
    @JsonProperty("app")
    private String app = null;

    @JsonProperty("uri")
    private String uri = null;

    @JsonProperty("hits")
    private Long hits = null;
}
