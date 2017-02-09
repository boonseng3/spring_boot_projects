package com.obs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        basePackages = "com.obs.datasource2.repo",
        entityManagerFactoryRef = "entityManagerFactory2",
        transactionManagerRef = "transactionManager2"
)
public class DataSourceConfig2 {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasources[1]")
    public DataSource secondaryDataSource1() {
        return DataSourceBuilder.create().build();
    }

    @Autowired
    EntityManagerFactoryBuilder builder;


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory2() {

        LocalContainerEntityManagerFactoryBean factory = builder
                .dataSource(secondaryDataSource1())
                .packages("com.obs.datasource2.entity")
                .persistenceUnit("ds2")
                .build();
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager2() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory2().getObject());
        return transactionManager;
    }

    @Bean
    public JdbcTemplate jdbcTemplate2() {
        return new JdbcTemplate(secondaryDataSource1());
    }
}
