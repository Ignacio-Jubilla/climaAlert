package ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Clima {
    private Long id;
    private Ubicacion ubicacion;
    private Double tempC;
    private Double tempF;
    private String condicion;
    private Double velocidadVientoKmh;
    private Integer humedad;
    private LocalDateTime fechaActualizacion;
    private boolean procesado;

    public Clima() {
        this.fechaActualizacion = LocalDateTime.now();
        this.procesado = false;
    }
}
