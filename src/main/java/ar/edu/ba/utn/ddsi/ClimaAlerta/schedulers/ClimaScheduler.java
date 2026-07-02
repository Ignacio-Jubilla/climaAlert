package ar.edu.ba.utn.ddsi.ClimaAlerta.schedulers;

import ar.edu.ba.utn.ddsi.ClimaAlerta.services.IClimaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClimaScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ClimaScheduler.class);
    private final IClimaService climaService;

    public ClimaScheduler(IClimaService climaService) {this.climaService = climaService;}

    @Scheduled(fixedRate = 300000)
    public void actualizarClima(){
        climaService.actualizarClimaCiudades()
                .doOnSuccess(v->logger.info("Actualizacion de {} completada",v))
                .doOnError(e->logger.error("Error en la actualizacion del clima: ",e.getMessage()))
                .subscribe();
    }

}
