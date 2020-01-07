package com.lschaan.cidadebaixa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CidadeBaixaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CidadeBaixaApplication.class, args);
	}

}
