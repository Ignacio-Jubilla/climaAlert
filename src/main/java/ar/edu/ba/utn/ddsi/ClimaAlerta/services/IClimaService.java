package ar.edu.ba.utn.ddsi.ClimaAlerta.services;

import reactor.core.publisher.Mono;

public interface IClimaService {
    //aca va a ser la integracion con weatherApi
    Mono<Void> actualizarClimaCiudades();

}
