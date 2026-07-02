package ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities;

import lombok.Data;

@Data
public class Email {
    private Long id;
    private String destinatario;
    private String remitente;
    private String asunto;
    private String contenido;
    private boolean enviado;

    public Email(String destinatario , String contenido, String asunto, String remitente) {
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.asunto = asunto;
        this.remitente = remitente;
        this.enviado = false;
    }

    public void enviar() {
        //TODO: se puede hacer un adapter, para usar las biblio que ya hacen envios de mail
    }

}
