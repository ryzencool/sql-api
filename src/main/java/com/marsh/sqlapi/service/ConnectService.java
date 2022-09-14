package com.marsh.sqlapi.service;

import com.marsh.sqlapi.controller.request.ConnectIsLiveReq;
import com.marsh.sqlapi.controller.request.CreateConnectReq;
import com.marsh.sqlapi.domain.ProjectDataSource;
import com.marsh.sqlapi.exception.ErrorCode;
import com.marsh.sqlapi.helper.RoutingDataSource;
import com.marsh.sqlapi.helper.RoutingDataSourceContext;
import com.marsh.zutils.exception.BaseBizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import static com.marsh.sqlapi.helper.RoutingDataSource.DATASOURCE_MAP;

@Service
@Slf4j
public class ConnectService {

    private final RoutingDataSource routingDataSource;
    private final JdbcTemplate jdbcTemplate;

    public ConnectService(RoutingDataSource routingDataSource, JdbcTemplate jdbcTemplate) {
        this.routingDataSource = routingDataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createConnect(CreateConnectReq req) {
        log.info("创建链接: {}", req.getDbName());
        RoutingDataSourceContext.setRouteKey(RoutingDataSourceContext.getMasterKey());
        var dsm = DATASOURCE_MAP.containsKey(req.getDbName());
        if (!dsm) {
            log.info("还没有当前的链接, {}", req.getDbName());
            String sql = "select name, url, username, password, db_type, project_id, id from project_data_source where name = ?";
            RowMapper<ProjectDataSource> rowMapper = new BeanPropertyRowMapper<>(ProjectDataSource.class);
            var res =  jdbcTemplate.queryForObject(sql, rowMapper, req.getDbName());
            if (res != null) {
                log.info("查询到记录：{}, {}, {}, {}, {}, {}", res.getName(), res.getUsername(), res.getPassword(), res.getDbType(), res.getHost(), res.getPort() );
                try (var ds = routingDataSource.createDruidDataSource(res)) {
                    routingDataSource.saveDataSource(req.getDbName(), ds);
                    log.info("链接成功");
                }
            } else {
                throw new BaseBizException(ErrorCode.DSDataNotFound);
            }
        }
    }

    public boolean isLive(ConnectIsLiveReq req) {
        log.info("当前链接是:{}", req.getDbName());
        var isIn =  DATASOURCE_MAP.containsKey(req.getDbName());
        log.info("当前是否在池中 {}", isIn);
        return isIn;
    }
}
