package ar.edu.ba.utn.ddsi.ClimaAlerta.schedulers;

import ar.edu.ba.utn.ddsi.ClimaAlerta.services.impl.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailScheduler {
    private final EmailService emailService;

    public EmailScheduler(EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(cron = "${cron.expression}")
    public void procesarEmailsPendientes(){
        emailService.procesarPendientes();
    }

    @Scheduled(cron= "${cron.expression}")
    public void loguearEmailsPendientes(){
        emailService.loguearEmailsPendientes();
    }

}
