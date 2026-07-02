package ar.edu.ba.utn.ddsi.ClimaAlerta.models.dto.external.weatherapi;

import lombok.Data;

@Data
public class WeatherResponse {
    private Location location;
    private Current current;
}
