package com.marsh.sqlapi.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({DBProperties.class})
public class PropertiesConfig {
}
