package com.marsh.sqlapi.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConfigurationProperties(prefix = "db")
public class DBProperties {

    private ChildDB main;

    private ChildDB pg;

    private ChildDB mysql;
}
