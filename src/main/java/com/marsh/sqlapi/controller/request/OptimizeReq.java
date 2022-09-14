package com.marsh.sqlapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptimizeReq {

    private String sql;

    private String dbName;

    private String username;

    private String password;

    private Integer dbType;

    private String host;

    private Integer port;
}
