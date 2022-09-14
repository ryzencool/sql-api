package com.marsh.sqlapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecSqlReq {

    private String sql;

    private Integer dbType;

    private String dbName;

}
