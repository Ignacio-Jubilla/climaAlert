package ar.edu.ba.utn.ddsi.ClimaAlerta.schedulers;

import ar.edu.ba.utn.ddsi.ClimaAlerta.services.IAlertaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlertasScheduler {
    private static final Logger logger = LoggerFactory.getLogger(AlertasScheduler.class);
    private final IAlertaService alertaService;

    public AlertasScheduler(IAlertaService alertaService) {this.alertaService = alertaService;}

    @Scheduled(fixedRate = 60000)
    public void procesarAlertas(){
        alertaService.generarAlertasYAvisar()
                .doOnSubscribe(s -> logger.info("Iniciando proceso de alertas"))
                .doOnSuccess(v->logger.info("Procesamiento de Alertas Completado"))
                .doOnError(e->logger.error("Error al procesar alertas: ",e))
                .subscribe();

}
}
