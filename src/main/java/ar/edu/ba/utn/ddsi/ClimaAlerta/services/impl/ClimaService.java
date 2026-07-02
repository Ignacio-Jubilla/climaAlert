package ar.edu.ba.utn.ddsi.ClimaAlerta.services.impl;

import ar.edu.ba.utn.ddsi.ClimaAlerta.models.dto.external.weatherapi.WeatherResponse;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Clima;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.entities.Ubicacion;
import ar.edu.ba.utn.ddsi.ClimaAlerta.models.repositories.IClimaRepository;
import ar.edu.ba.utn.ddsi.ClimaAlerta.services.IClimaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class ClimaService implements IClimaService {

    private static final Logger logger = LoggerFactory.getLogger(ClimaService.class);
    private static final String[] CIUDADES_ARGENTINA = {
            "Buenos Aires","Cordoba","Rosario","La Pampa, argentina"
    };

    private final IClimaRepository climaRepository;
    private final WebClient webClient;
    private final String apiKey;

    public ClimaService(
            IClimaRepository climaRepository,
            @Value("${weather.api.key}") String apiKey,
            @Value("${weather.api.base-url}") String baseUrl){
        this.climaRepository = climaRepository;
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public Mono<List<Ubicacion>> actualizarClimaCiudades() {
        return Flux.fromArray(CIUDADES_ARGENTINA)
                .flatMap(this::obtenerClimaDeApi)
                .flatMap(clima -> {
                    Clima climaGuardado = climaRepository.save(clima);

                    logger.info(
                            "Clima actualizado para: {}\nLa temperatura en grados C es de {}",
                            climaGuardado.getUbicacion().getCiudad(),
                            climaGuardado.getTempC()
                    );

                    return Mono.just(climaGuardado.getUbicacion());
                })
                .collectList()
                .onErrorResume(e -> {
                    logger.error("Error al actualizar clima: {}", e.getMessage());
                    return Mono.just(Collections.emptyList());
                });
    }

    private Mono<Clima> obtenerClimaDeApi(String ciudad){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/current.json")
                        .queryParam("key",apiKey)
                        .queryParam("q",ciudad)
                        .queryParam("aqi","no")
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .map(response-> {
                    Clima clima = new Clima();
                    Ubicacion ubicacion = new Ubicacion();
                    ubicacion.setCiudad(ciudad);
                    ubicacion.setRegion(response.getLocation().getRegion());
                    ubicacion.setPais(response.getLocation().getCountry());
                    clima.setUbicacion(ubicacion);
                    clima.setTempC(response.getCurrent().getTemp_c());
                    clima.setTempF(response.getCurrent().getTemp_f());
                    clima.setCondicion(response.getCurrent().getCondition().getText());
                    clima.setVelocidadVientoKmh(response.getCurrent().getWind_kmh());
                    clima.setHumedad(response.getCurrent().getHumidity());
                    return clima;
                });
    }
}
