package ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

@Data
public class Email {
    private Long id;
    private String destinatario;
    private String remitente;
    private String asunto;
    private String contenido;
    private boolean enviado;
    private LocalDateTime fechaEnvio;
    private Alerta alerta;

    public Email(String destinatario, String contenido, String asunto, String remitente) {
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.asunto = asunto;
        this.remitente = remitente;
        this.enviado = false;
    }

    public void enviar(JavaMailSender mailSender) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(this.destinatario.split(","));
        mensaje.setSubject("⚠️ Alerta climática - Climalert: "+ this.asunto);
        mensaje.setText(construirCuerpo());
        mailSender.send(mensaje);

        this.enviado = true;
        this.fechaEnvio = LocalDateTime.now();
    }

    private String construirCuerpo() {
        return this.contenido;
    }
}


