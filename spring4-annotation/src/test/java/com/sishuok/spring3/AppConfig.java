package com.sishuok.spring3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-24
 * <p>Version: 1.0
 */
@Profile("dev")
@Configuration
@PropertySource(value = "classpath:resources.properties", ignoreResourceNotFound = false)
public class AppConfig {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
