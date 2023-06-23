package com.demo.buslinesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@CrossOrigin(origins = {"http://localhost:8080", "null"})
@RestController
public class BusLineSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusLineSystemApplication.class, args);
    }
}
