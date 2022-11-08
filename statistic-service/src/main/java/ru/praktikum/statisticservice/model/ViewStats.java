package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ViewStats
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-11-08T15:34:45.022Z[GMT]")


public class ViewStats   {
  @JsonProperty("app")
  private String app = null;

  @JsonProperty("uri")
  private String uri = null;

  @JsonProperty("hits")
  private Long hits = null;

  public ViewStats app(String app) {
    this.app = app;
    return this;
  }

  /**
   * Название сервиса
   * @return app
   **/
  @Schema(description = "Название сервиса")
  
    public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }

  public ViewStats uri(String uri) {
    this.uri = uri;
    return this;
  }

  /**
   * URI сервиса
   * @return uri
   **/
  @Schema(description = "URI сервиса")
  
    public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public ViewStats hits(Long hits) {
    this.hits = hits;
    return this;
  }

  /**
   * Количество просмотров
   * @return hits
   **/
  @Schema(description = "Количество просмотров")
  
    public Long getHits() {
    return hits;
  }

  public void setHits(Long hits) {
    this.hits = hits;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ViewStats viewStats = (ViewStats) o;
    return Objects.equals(this.app, viewStats.app) &&
        Objects.equals(this.uri, viewStats.uri) &&
        Objects.equals(this.hits, viewStats.hits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(app, uri, hits);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ViewStats {\n");
    
    sb.append("    app: ").append(toIndentedString(app)).append("\n");
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    hits: ").append(toIndentedString(hits)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
