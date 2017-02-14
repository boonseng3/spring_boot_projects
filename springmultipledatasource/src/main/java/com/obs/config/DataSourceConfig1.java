package com.obs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by ongbo on 2/8/2017.
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "com.obs.datasource1.repo",
        entityManagerFactoryRef = "entityManagerFactory1",
        transactionManagerRef = "transactionManager1"
)
public class DataSourceConfig1 {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${spring.datasources[0].type}")
    String dataSourceType;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasources[0]")
    public DataSource primaryDataSource() {
        try {
            return DataSourceBuilder.create().type((Class<? extends DataSource>) Class.forName(dataSourceType)).build();
        } catch (ClassNotFoundException e) {
            logger.error("Exception creating dataSource", e);
        }
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory1(EntityManagerFactoryBuilder builder) {

        LocalContainerEntityManagerFactoryBean factory = builder
                .dataSource(primaryDataSource())
                .packages("com.obs.datasource1.entity")
                .persistenceUnit("ds1")
                .build();
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager1(EntityManagerFactoryBuilder builder) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory1(builder).getObject());
        return transactionManager;
    }

    @Bean
    public JdbcTemplate jdbcTemplate1() {
        return new JdbcTemplate(primaryDataSource());
    }
}
