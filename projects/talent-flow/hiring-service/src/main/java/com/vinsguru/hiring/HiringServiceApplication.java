package com.vinsguru.hiring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class HiringServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HiringServiceApplication.class, args);
    }

}