package ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.impl;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Alerta;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Email;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.IAlertaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AlertaRepository implements IAlertaRepository {

    private final Map<Long, Alerta> alertas = new HashMap<>();
    private final AtomicLong idGenerator= new AtomicLong(1);

    public Alerta save(Alerta alerta) {
        if(alerta.getIdAlerta()==null){
            alerta.setIdAlerta(idGenerator.getAndIncrement());
            alertas.put(alerta.getIdAlerta(),alerta);
        }else{
            alertas.put(alerta.getIdAlerta(),alerta);
        }
        return alerta;
    }

    public List<Alerta> findAll() {
        return new ArrayList<>(alertas.values());
    }
}
