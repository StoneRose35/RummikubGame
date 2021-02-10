package ch.sr35.rummikub.web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RummikubApi {
	

	public static void main(String[] args) {
		SpringApplication.run(RummikubApi.class, args);
	}
	
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Happily serving a virtual brain for playing rummikub");
		};
	}

}
