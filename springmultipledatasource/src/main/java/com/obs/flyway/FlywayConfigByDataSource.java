package com.obs.flyway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ongbo on 2/10/2017.
 */
@Component
@ConfigurationProperties(prefix = "flyway")
public class FlywayConfigByDataSource {
    private List<FlywayByDataSource> datasources = new ArrayList<>();

    public List<FlywayByDataSource> getDatasources() {
        return datasources;
    }

    public FlywayConfigByDataSource setDatasources(List<FlywayByDataSource> datasources) {
        this.datasources = datasources;
        return this;
    }
}
