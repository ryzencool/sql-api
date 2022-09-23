package com.marsh.sqlapi.service;

import com.marsh.sqlapi.controller.request.ExecSqlReq;
import com.marsh.sqlapi.helper.RoutingDataSourceContext;
import com.marsh.sqlapi.result.SqlResult;
import com.marsh.zutils.exception.BaseBizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class SqlExecService {

    private final JdbcTemplate jdbcTemplate;

    public SqlExecService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SqlResult execSql(ExecSqlReq req) {
        log.info("执行sql: {}, {}, {}", req.getDbName(), req.getDbType(), req.getSql());
        RoutingDataSourceContext.setRouteKey(req.getDbName());
        var sql = req.getSql();
        var res = new SqlResult();
        try {
            Object result = null;
            if (sql.startsWith("select") || sql.startsWith("explain")) {
                result = jdbcTemplate.queryForList(sql);
            } else if (sql.startsWith("insert") || sql.startsWith("update") || sql.startsWith("delete")) {
                result = jdbcTemplate.update(sql);
            } else {
                if (sql.contains(";")) {
                    var temps = sql.split(";");
                    for (String temp : temps) {
                        var t = StringUtils.trimLeadingWhitespace(temp);
                        t = StringUtils.trimTrailingWhitespace(t);
                        t = t.replace("`", "");
                        t = t.replace("\n", " ");
                        if (t.length() == 0) {
                            continue;
                        }
                        jdbcTemplate.execute(t);
                    }
                } else {
                    jdbcTemplate.execute(sql);
                }
            }
            res.setResult(result);
        } catch (Exception e) {
            log.info("出现错误, {}", e.getMessage());
            throw new BaseBizException("111112", e.getMessage());
        }
        return res;
    }
}
