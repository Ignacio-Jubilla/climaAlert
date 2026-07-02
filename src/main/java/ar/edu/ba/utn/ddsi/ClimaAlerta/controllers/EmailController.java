package ar.edu.ba.utn.ddsi.ClimaAlerta.controllers;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Email;
import ar.edu.ba.utn.ddsi.ClimaAlerta.services.IEmailService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
public class EmailController {
    private IEmailService emailService;

    public EmailController(IEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public Email crearEmail(@RequestBody Email email) {
        return this.emailService.createEmail(email);

    }

    @GetMapping
    public List<Email> obtenerEmails(@RequestParam(required = false) Boolean pendiente) {
        return this.emailService.getEmails(pendiente);
    }

}
