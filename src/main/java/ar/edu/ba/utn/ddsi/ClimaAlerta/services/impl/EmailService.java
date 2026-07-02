package ar.edu.ba.utn.ddsi.ClimaAlerta.services.impl;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Alerta;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Clima;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Email;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.impl.AlertaRepository;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.impl.EmailRepository;
import ar.edu.ba.utn.ddsi.ClimaAlerta.services.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService implements IEmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final EmailRepository emailRepository;
    private final JavaMailSender mailSender;
    private final AlertaRepository alertaRepository;

    @Value("${climalert.mail.destinatarios}")
    private String destinatariosAlerta;

    public EmailService(EmailRepository emailRepository, JavaMailSender mailSender , AlertaRepository alertaRepository) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
        this.alertaRepository = alertaRepository;
    }

    @Override
    public Email createEmail(Email email) {
        return this.emailRepository.save(email);
    }

    @Override
    public List<Email> getEmails(Boolean pendiente) {
        if(pendiente!=null){
            return emailRepository.findByEnviado(!pendiente);
        }else{
            return emailRepository.findAll();
        }
    }

    @Override
    public void procesarPendientes() {
        //Éste el el method que se llama desde el cronJob
        logger.info("Procesando pendientes");
        List<Email> pendientes = this.emailRepository.findByEnviado(false);
        for(Email email : pendientes){
            try {
                email.enviar(mailSender);
                emailRepository.save(email);
                actualizarEstadoAlerta(email.getAlerta());
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
    }

    @Override
    public void loguearEmailsPendientes() {
        List<Email> pendientes = this.emailRepository.findByEnviado(false);
        logger.info("Emails pendientes de envio: {}",pendientes.size());
        pendientes.forEach(email -> {
            logger.info("Email pendiente - ID {}. destinatario: {}. asunto: {}",
                    email.getId(),
                    email.getDestinatario(),
                    email.getAsunto());
        });
    }

    private void actualizarEstadoAlerta(Alerta alerta) {
        boolean todosEnviados = emailRepository.findByAlerta(alerta.getIdAlerta()).stream()
                .allMatch(Email::isEnviado);
        if (todosEnviados) {
            alerta.setEnviada(true);
            alertaRepository.save(alerta);
        }
    }
    public void generarYEnviarEmails(Clima clima, List<String> destinatarios, String remitente){
        String asunto = "Clima Alertaa";
        String mensaje = String.format(
                "ALERTA: Condiciones climaticas extremas detectadas en %s\n\n"+
                        "Temperatura: %.1f*C\n"+
                        "Humedad: %d%%\n",
                clima.getUbicacion(),
                clima.getTempC(),
                clima.getHumedad()
        );

        for(String destinatario : destinatarios){
            Email email = new Email(destinatario,mensaje,asunto,remitente);
            this.createEmail(email);
        }

        logger.info("Email de alerta generado para {} - Enviado a {} destinatarios",
                clima.getUbicacion(),destinatarios.size());
    }

}
