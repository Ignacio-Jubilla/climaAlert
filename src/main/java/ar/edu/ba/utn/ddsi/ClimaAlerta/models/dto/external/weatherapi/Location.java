package ar.edu.ba.utn.ddsi.ClimaAlerta.models.dto.external.weatherapi;

import lombok.Data;

@Data
public class Location {
    private String name;
    private String region;
    private String country;
}
