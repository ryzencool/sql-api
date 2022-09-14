package com.marsh.sqlapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDataSource {

    private Integer id;

    private Integer projectId;

    private String url;

    private String host;

    private Integer port;
    
    private String name;

    private String username;

    private String password;

    private Integer dbType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer createId;

    private Integer updateId;

    private Boolean isDel;
}
