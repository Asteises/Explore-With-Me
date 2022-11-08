/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.35).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.ViewStats;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-11-08T15:34:45.022Z[GMT]")
@Validated
public interface StatsApi {

    @Operation(summary = "Получение статистики по посещениям. Обратите внимание: значение даты и времени нужно закодировать (например используя java.net.URLEncoder.encode) ", description = "", tags={ "StatsController" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Статистика собрана", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ViewStats.class)))) })
    @RequestMapping(value = "/stats",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<ViewStats>> getStats(@NotNull @Parameter(in = ParameterIn.QUERY, description = "Дата и время начала диапазона за который нужно выгрузить статистику (в формате \"yyyy-MM-dd HH:mm:ss\")" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "start", required = true) String start, @NotNull @Parameter(in = ParameterIn.QUERY, description = "Дата и время конца диапазона за который нужно выгрузить статистику (в формате \"yyyy-MM-dd HH:mm:ss\")" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "end", required = true) String end, @Parameter(in = ParameterIn.QUERY, description = "Список uri для которых нужно выгрузить статистику" ,schema=@Schema()) @Valid @RequestParam(value = "uris", required = false) List<String> uris, @Parameter(in = ParameterIn.QUERY, description = "Нужно ли учитывать только уникальные посещения (только с уникальным ip)" ,schema=@Schema( defaultValue="false")) @Valid @RequestParam(value = "unique", required = false, defaultValue="false") Boolean unique);

}

