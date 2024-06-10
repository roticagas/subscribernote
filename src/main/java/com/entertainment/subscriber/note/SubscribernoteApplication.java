package com.entertainment.subscriber.note;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class SubscribernoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscribernoteApplication.class, args);
    }

}
