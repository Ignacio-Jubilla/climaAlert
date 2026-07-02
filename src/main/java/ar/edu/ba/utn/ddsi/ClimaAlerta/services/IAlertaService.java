package ar.edu.ba.utn.ddsi.ClimaAlerta.services;

import reactor.core.publisher.Mono;

public interface IAlertaService {

    public Mono<Void> generarAlertasYAvisar();
}
