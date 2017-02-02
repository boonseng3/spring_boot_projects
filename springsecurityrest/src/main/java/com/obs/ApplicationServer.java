package com.obs;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ApplicationServer {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(ApplicationServer.class)
                .properties("spring.config.name=server").run(args);
    }
}
