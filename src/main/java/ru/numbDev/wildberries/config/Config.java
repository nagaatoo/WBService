package ru.numbDev.wildberries.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Configuration
@EnableScheduling
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class Config {

//    @Bean(name="customDataSource")
//    @ConfigurationProperties("spring.datasource")
//    public DataSource customDataSource() {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
//        dataSourceBuilder.url("jdbc:sqlite:your.db");
//        return dataSourceBuilder.build();
//    }
}
