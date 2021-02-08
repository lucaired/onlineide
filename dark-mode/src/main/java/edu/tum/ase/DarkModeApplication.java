package edu.tum.ase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DarkModeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DarkModeApplication.class, args);
    }

}