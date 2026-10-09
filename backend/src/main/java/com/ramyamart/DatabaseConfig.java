package com.ramyamart;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url:}")
    private String url;

    @Value("${spring.datasource.username:root}")
    private String username;

    @Value("${spring.datasource.password:}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        String dbUrl = url;
        if (dbUrl != null && !dbUrl.trim().isEmpty()) {
            dbUrl = dbUrl.trim();
            if (dbUrl.startsWith("mysql://")) {
                dbUrl = "jdbc:" + dbUrl;
            }
            if (dbUrl.contains("ssl-mode=")) {
                dbUrl = dbUrl.replace("ssl-mode=", "sslMode=");
            }
        } else {
            dbUrl = "jdbc:mysql://localhost:3306/ramya_mart";
        }

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.addDataSourceProperty("sslMode", "PREFERRED");
        ds.addDataSourceProperty("allowPublicKeyRetrieval", "true");
        ds.setMaximumPoolSize(5);
        ds.setMinimumIdle(1);
        ds.setConnectionTimeout(30000);
        ds.setIdleTimeout(600000);
        ds.setMaxLifetime(1800000);
        return ds;
    }
}
