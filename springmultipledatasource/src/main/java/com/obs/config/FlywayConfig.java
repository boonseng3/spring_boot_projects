package com.obs.config;

import com.obs.flyway.CustomFlywayMigrationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ongbo on 2/9/2017.
 */
@Configuration
public class FlywayConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return new CustomFlywayMigrationStrategy();
    }
}
