package ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.impl;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Email;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.IEmailRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EmailRepository implements IEmailRepository {

    private final Map<Long,Email> emails= new HashMap<>();
    private final AtomicLong idGenerator= new AtomicLong(1);

    @Override
    public Email save(Email email) {
        if(email.getId()==null){
            Long id= idGenerator.getAndIncrement();
            email.setId(id);
            emails.put(id,email);
        }else{
            emails.put(email.getId(),email);
        }
        return email;
    }

    @Override
    public List<Email> findAll() {
        return new ArrayList<>(emails.values());
    }

    @Override
    public List<Email> findByEnviado(boolean enviado) {
        return emails.values().stream()
                .filter(email -> email.isEnviado()==enviado)
                .toList();
    }
}
