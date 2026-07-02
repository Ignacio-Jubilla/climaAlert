package ar.edu.ba.utn.ddsi.ClimaAlerta.services;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Clima;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Email;

import java.util.List;

public interface IEmailService {
    Email createEmail(Email email);
    List<Email> getEmails(Boolean pendiente);
    void procesarPendientes();
    void loguearEmailsPendientes();
    void generarYEnviarEmails(Clima clima, List<String> destinatarios, String remitente);
}
