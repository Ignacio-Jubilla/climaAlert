package ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.impl;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Clima;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.IClimaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ClimaRepository implements IClimaRepository {

    private final Map<Long,Clima> climas= new HashMap<>();
    private final Map<String,Long> ciudadToId= new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Clima save(Clima clima) {
        if(clima.getId()==null){
            Long id= idGenerator.getAndIncrement();
            clima.setId(id);
            climas.put(id,clima);
            ciudadToId.put(clima.getCiudad(),id);
        }else{
            climas.put(clima.getId(),clima);
        }
        return clima;
    }

    @Override
    public List<Clima> findByProcesado(boolean procesado) {
        return climas.values().stream()
                .filter(clima -> clima.isProcesado()==procesado)
                .toList();
    }
}
