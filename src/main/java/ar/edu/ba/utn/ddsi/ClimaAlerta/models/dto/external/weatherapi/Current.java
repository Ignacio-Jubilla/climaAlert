package ar.edu.ba.utn.ddsi.ClimaAlerta.models.dto.external.weatherapi;

import lombok.Data;

@Data
public class Current {
    private double temp_c;
    private double temp_f;
    private Condition condition;
    private double wind_kmh;
    private int humidity;
}
