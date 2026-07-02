package ar.edu.ba.utn.ddsi.ClimaAlerta.services.impl;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Email;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.impl.EmailRepository;
import ar.edu.ba.utn.ddsi.ClimaAlerta.services.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService implements IEmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailRepository emailRepository;

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
                email.enviar();
                email.setEnviado(true);
                emailRepository.save(email);
            }catch (Exception e){
                e.printStackTrace();
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
}
