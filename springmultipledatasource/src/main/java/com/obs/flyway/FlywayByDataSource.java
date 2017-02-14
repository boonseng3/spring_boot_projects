package com.obs.flyway;

import org.flywaydb.core.Flyway;

/**
 * Created by ongbo on 2/10/2017.
 */
public class FlywayByDataSource extends Flyway {
    // Use url to match the flyway config to the correct dataSource
    private String url;

    public String getUrl() {
        return url;
    }

    public FlywayByDataSource setUrl(String url) {
        this.url = url;
        return this;
    }
}
