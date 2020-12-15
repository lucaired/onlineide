package edu.tum.ase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Note the specified base packages to use the shared error handler in a separate project
@SpringBootApplication(scanBasePackages = "edu.tum.ase")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
