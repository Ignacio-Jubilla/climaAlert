package ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Clima;

import java.util.List;

public interface IClimaRepository {
    Clima save(Clima clima);
    List<Clima> findByProcesado(boolean procesado);
    // entre otros
}
