package ar.edu.ba.utn.ddsi.ClimaAlerta.services;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Ubicacion;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IClimaService {
    //aca va a ser la integracion con weatherApi
    Mono<List<Ubicacion>> actualizarClimaCiudades();

}
