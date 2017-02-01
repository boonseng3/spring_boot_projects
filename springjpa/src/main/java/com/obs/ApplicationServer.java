package com.obs;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApplicationServer {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(ApplicationServer.class)
                .properties("spring.config.name=server").run(args);
    }
}
