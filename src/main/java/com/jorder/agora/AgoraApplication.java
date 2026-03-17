package com.jorder.agora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgoraApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgoraApplication.class, args);
		System.out.println("Aplicação Agora iniciada com sucesso!");
	}

}
