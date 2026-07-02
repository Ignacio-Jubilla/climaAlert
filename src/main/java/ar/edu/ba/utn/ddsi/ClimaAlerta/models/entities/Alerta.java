package ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Alerta {
    private Long idAlerta;
    private String mensaje;
    private LocalDate fechaAlerta;
    private Clima condicionesAlerta;
    private Boolean enviada;

    public Alerta(LocalDate fechaAlerta, Clima condicionesAlerta, Boolean enviada) {

    }
}
