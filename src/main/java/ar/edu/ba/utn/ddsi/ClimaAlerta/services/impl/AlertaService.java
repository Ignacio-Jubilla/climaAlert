package ar.edu.ba.utn.ddsi.ClimaAlerta.services.impl;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Alerta;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Clima;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Email;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.IAlertaRepository;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.IClimaRepository;
import ar.edu.ba.utn.ddsi.ClimaAlerta.services.IAlertaService;
import ar.edu.ba.utn.ddsi.ClimaAlerta.services.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class AlertaService implements IAlertaService {

    private static final Logger logger = LoggerFactory.getLogger(AlertaService.class);
    private static final double TEMPERATURA_ALERTA = 35;
    private static final int HUMEDAD_ALERTA = 60;

    private final IClimaRepository climaRepository;
    private final IEmailService emailService;
    private final String remitente;
    private final List<String> destinatarios;
    private final IAlertaRepository alertaRepository;

    public AlertaService(IClimaRepository climaRepository, EmailService emailService,
                         @Value("${climalert.mail.remitente}") String remitente,
                         @Value("${climalert.mail.destinatarios}") List<String> destinatarios,
                         IAlertaRepository alertaRepository) {
        this.climaRepository = climaRepository;
        this.emailService = emailService;
        this.remitente = remitente;
        this.destinatarios = destinatarios;
        this.alertaRepository = alertaRepository;
    }

    @Override
    public Mono<List<Alerta>> generarAlertasYAvisar() {
        return Mono.fromCallable(() -> climaRepository.findByProcesado(false))
                .map(climas -> {
                    logger.info("Procesando {} registros de climas no procesados", climas.size());

                    List<Alerta> alertas = climas.stream()
                            .filter(this::cumpleCondicionesAlerta)
                            .map(this::generarAlerta)
                            .toList();

                    climas.forEach(clima -> {
                        clima.setProcesado(true);
                        climaRepository.save(clima);
                    });

                    return alertas;
                })
                .onErrorResume(e -> {
                    logger.error("Error al generar alertas de climas no procesados", e);
                    return Mono.just(Collections.emptyList());
                });
    }

    public boolean cumpleCondicionesAlerta(Clima clima) {
        return clima.getTempC()>TEMPERATURA_ALERTA &&
                clima.getHumedad()>HUMEDAD_ALERTA;
    }


    public Alerta generarAlerta(Clima clima){
        Alerta nuevaAlerta = new Alerta(
                LocalDate.now(),
                clima,
                false
        );
        alertaRepository.save(nuevaAlerta);
        emailService.generarYEnviarEmails(clima,destinatarios,remitente);
        return nuevaAlerta;
    }

}

