package ar.edu.ba.utn.ddsi.ClimaAlerta.services;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Alerta;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Clima;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IAlertaService {

    Mono<List<Alerta>> generarAlertasYAvisar();
    boolean cumpleCondicionesAlerta(Clima clima);
    Alerta generarAlerta(Clima clima);
    }
