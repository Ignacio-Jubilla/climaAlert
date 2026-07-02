package ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Alerta;

import java.util.List;

public interface IAlertaRepository {
    Alerta save(Alerta alerta);
    List<Alerta> findAll();
}
