package com.dwes.security.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Evita errores si la API envía campos extra
public class WeatherDTO {

    private Double latitude;
    private Double longitude;

    // Open-Meteo usa "current_weather" si se pide con ese parámetro
    // o "current" dependiendo de la versión. Ponemos ambos por seguridad.
    @JsonProperty("current_weather")
    private CurrentData current;

    @JsonProperty("daily")
    private DailyData daily;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentData {
        @JsonProperty("temperature") // En current_weather se llama temperature
        public Double temperature;
        
        @JsonProperty("weathercode")
        public Integer weatherCode;
        
        public Integer is_day;
        public String time;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DailyData {
        @JsonProperty("temperature_2m_max")
        public List<Double> maxTemperatures;
        
        @JsonProperty("temperature_2m_min")
        public List<Double> minTemperatures;
    }
}