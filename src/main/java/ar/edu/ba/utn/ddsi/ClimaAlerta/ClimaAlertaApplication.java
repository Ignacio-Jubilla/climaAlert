package ar.edu.ba.utn.ddsi.ClimaAlerta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClimaAlertaApplication {

	public static void main(String[] args) {

		SpringApplication.run(ClimaAlertaApplication.class, args);
	}

}
