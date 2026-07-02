package ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Email;

import java.util.List;

public interface IEmailRepository {
    Email save(Email email);
    List<Email> findAll();
    List<Email> findByEnviado(boolean enviado);
    // algunos mas

}
