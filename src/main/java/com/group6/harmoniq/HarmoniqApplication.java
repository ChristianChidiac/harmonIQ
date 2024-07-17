package com.group6.harmoniq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication
public class HarmoniqApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		dotenv.entries().forEach(entry -> {
		System.setProperty(entry.getKey(), entry.getValue());
	   });
		SpringApplication.run(HarmoniqApplication.class, args);
	}

}
