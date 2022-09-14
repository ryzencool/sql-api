package com.marsh.sqlapi.controller;

import com.marsh.sqlapi.controller.request.ExecSqlReq;
import com.marsh.sqlapi.service.SqlExecService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SqlExecController {

    private final SqlExecService sqlExecService;


    public SqlExecController(SqlExecService sqlExecService) {
        this.sqlExecService = sqlExecService;
    }

    @PostMapping("/executeSql")
    public ResponseEntity<Map<String, Object>> execSql(@RequestBody ExecSqlReq req) {
        var res = sqlExecService.execSql(req);
        return ResponseEntity.ok(Map.of("code", "000000", "data", res));
    }
}
