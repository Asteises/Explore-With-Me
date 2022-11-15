package ru.praktikum.statisticservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

/**
 * EndpointHit
 */
@Validated
public class EndpointHit {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("app")
    private String app = null;

    @JsonProperty("uri")
    private String uri = null;

    @JsonProperty("ip")
    private String ip = null;

    @JsonProperty("timestamp")
    private String timestamp = null;
}
