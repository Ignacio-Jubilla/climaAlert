package ar.edu.ba.utn.ddsi.ClimaAlerta.services.impl;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Clima;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Email;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.IClimaRepository;
import ar.edu.ba.utn.ddsi.ClimaAlerta.services.IAlertaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.util.List;

public class AlertaService implements IAlertaService {

    private static final Logger logger = LoggerFactory.getLogger(AlertaService.class);
    private static final double TEMPERATURA_ALERTA = 35.0;
    private static final int HUMEDAD_ALERTA = 60;

    private final IClimaRepository climaRepository;
    private final EmailService emailService;
    private final String remitente;
    private final List<String> destinatarios;

    public AlertaService(IClimaRepository climaRepository, EmailService emailService, @Value("${email.alertas.remitente}") String remitente,@Value("${email.alertas.destanatarios}") List<String> destinatarios) {
        this.climaRepository = climaRepository;
        this.emailService = emailService;
        this.remitente = remitente;
        this.destinatarios = destinatarios;
    }

    @Override
    public Mono<Void> generarAlertasYAvisar() {
        return Mono.fromCallable(()-> climaRepository.findByProcesado(false))
                .flatMap(climas -> {
                    logger.info("Procesamiento {} registros de climas no procesados",climas.size());
                    return Mono.just(climas);
                })
                .flatMap(climas -> {
                    climas.stream()
                            .filter(this::cumpleCondicionesAlerta)
                            .forEach(this::generarYEnviarEmails);
                    climas.forEach(clima -> {
                        clima.setProcesado(true);
                        climaRepository.save(clima);
                    });
                    return Mono.empty();
                })
                .onErrorResume(e-> {
                    logger.error("Error al generar alertas de climas no procesados", e.getMessage());
                    return Mono.empty();
                })
                .then();
    }

    private boolean cumpleCondicionesAlerta(Clima clima) {
        return clima.getTempC()>TEMPERATURA_ALERTA &&
                clima.getHumedad()>HUMEDAD_ALERTA;
    }

    private void generarYEnviarEmails(Clima clima){
        String asunto = "Clima Alertaa";
        String mensaje = String.format(
                "ALERTA: Condiciones climaticas extremas detectadas en %s\n\n"+
                "Temperatura: %.1f*C\n"+
                "Humedad: %d%%\n",
                clima.getCiudad(),
                clima.getTempC(),
                clima.getHumedad()
        );

        for(String destinatario : destinatarios){
            Email email = new Email(destinatario,remitente,asunto,mensaje);
            emailService.createEmail(email);
        }

        logger.info("Email de alerta generado para {} - Enviado a {} destinatarios",
                clima.getCiudad(),destinatarios.size());
    }

}

