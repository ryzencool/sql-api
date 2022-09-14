package com.marsh.sqlapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserReq {

    private String username;

    private String password;

    private String dbType;

    private String dbName;
}
