package com.obs.config;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasources[0]")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Autowired
    EntityManagerFactoryBuilder builder;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory1() {

        LocalContainerEntityManagerFactoryBean factory = builder
                .dataSource(primaryDataSource())
                .packages("com.obs.datasource1.entity")
                .persistenceUnit("ds1")
                .build();
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager1() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory1().getObject());
        return transactionManager;
    }

    @Bean
    public JdbcTemplate jdbcTemplate1() {
        return new JdbcTemplate(primaryDataSource());
    }
}
