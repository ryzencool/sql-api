package com.marsh.sqlapi.helper;

import com.alibaba.druid.pool.DruidDataSource;
import com.marsh.sqlapi.config.DBProperties;
import com.marsh.sqlapi.domain.ProjectDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RoutingDataSource extends AbstractRoutingDataSource {

    private  JdbcTemplate jdbcTemplate;

    private final DBProperties dbProperties;


    public static Map<Object, Object> DATASOURCE_MAP = new ConcurrentHashMap<>();


    public RoutingDataSource(DBProperties dbProperties) {
        this.dbProperties = dbProperties;
        log.info("初始化动态数据源");
        createAndSaveDataSource(RoutingDataSourceContext.getMasterKey());
        createAndSaveDataSource(RoutingDataSourceContext.getPgMainKey());
        createAndSaveDataSource(RoutingDataSourceContext.getMysqlMainKey());

        DruidDataSource dataSource = getDataSource(RoutingDataSourceContext.getMasterKey());
        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String tenantId = RoutingDataSourceContext.getRouteKey();
        if (StringUtils.isEmpty(tenantId)) {
            tenantId = RoutingDataSourceContext.getMasterKey();
        }
        log.info("当前操作账套:{}", tenantId);
        if (!DATASOURCE_MAP.containsKey(tenantId)) {
            log.info("{}数据源不存在, 创建对应的数据源", tenantId);
            createAndSaveDataSource(tenantId);
        } else {
            log.info("{}数据源已存在不需要创建", tenantId);
        }
        log.info("切换到{}数据源", tenantId);
        return tenantId;
    }

    public void createAndSaveDataSource(String tenantId) {
        DruidDataSource dataSource = createDataSource(tenantId);
        DATASOURCE_MAP.put(tenantId, dataSource);
        super.setTargetDataSources(DATASOURCE_MAP);
        afterPropertiesSet();
    }

    public void saveDataSource(String tenantId,DruidDataSource dataSource) {
        DATASOURCE_MAP.put(tenantId, dataSource);
        super.setTargetDataSources(DATASOURCE_MAP);
        afterPropertiesSet();
    }

    public void closeDataSource(String tenantId) {
        var ds = DATASOURCE_MAP.get(tenantId);
        var dsObj = (DruidDataSource) ds;
        dsObj.close();
        DATASOURCE_MAP.remove(tenantId);
    }

    private DruidDataSource createDataSource(String tenantId) {

        ProjectDataSource projectDataSource;
        if (tenantId.equalsIgnoreCase(RoutingDataSourceContext.getPgMainKey())) {
            projectDataSource = new ProjectDataSource();
            projectDataSource.setName(RoutingDataSourceContext.getPgMainKey());
            projectDataSource.setDbType(2);
            projectDataSource.setUrl(dbProperties.getPg().getUrl());
            projectDataSource.setUsername(dbProperties.getPg().getUsername());
            projectDataSource.setPassword(dbProperties.getPg().getPassword());
        }
        else if (tenantId.equalsIgnoreCase(RoutingDataSourceContext.getMasterKey())) {
            projectDataSource = new ProjectDataSource();
            projectDataSource.setUrl(dbProperties.getMain().getUrl());
            projectDataSource.setName(RoutingDataSourceContext.getMasterKey());
            projectDataSource.setDbType(2);
            projectDataSource.setUsername(dbProperties.getMain().getUsername());
            projectDataSource.setPassword(dbProperties.getMain().getPassword());
        }
        else if (tenantId.equalsIgnoreCase(RoutingDataSourceContext.getMysqlMainKey())) {
            projectDataSource = new ProjectDataSource();
            projectDataSource.setName(RoutingDataSourceContext.getMysqlMainKey());
            projectDataSource.setDbType(1);
            projectDataSource.setUrl(dbProperties.getMysql().getUrl());
            projectDataSource.setUsername(dbProperties.getMysql().getUsername());
            projectDataSource.setPassword(dbProperties.getMysql().getPassword());
        } else {
            projectDataSource = getDataSourceProperties(tenantId);
        }

        if (projectDataSource == null) {
            throw new InvalidParameterException("租户不存在");
        }
        return createDruidDataSource(projectDataSource);
    }


    // 获取对应的配置
    private ProjectDataSource getDataSourceProperties(String name) {
        RoutingDataSourceContext.setRouteKey(RoutingDataSourceContext.getMasterKey());
        String sql = "select name, url, username, password, db_type, project_id, id from project_data_source where name = ?";
        RowMapper<ProjectDataSource> rowMapper = new BeanPropertyRowMapper<>(ProjectDataSource.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    public  DruidDataSource createDruidDataSource(ProjectDataSource ds) {
        DruidDataSource dataSource = new DruidDataSource();
        if (ds.getDbType() == 2) {
            dataSource.setDriverClassName("org.postgresql.Driver");
        }
        else if (ds.getDbType() == 1){
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        }
        dataSource.setName(ds.getName());
        dataSource.setUrl(ds.getUrl());
        dataSource.setUsername(ds.getUsername());
        dataSource.setPassword(ds.getPassword());

        dataSource.setInitialSize(2);
        // 从池中取得链接时做健康检查，该做法十分保守
        dataSource.setTestOnBorrow(true);
        // 如果连接空闲超过1小时就断开
        dataSource.setMinEvictableIdleTimeMillis(1 * 60000 * 60);
        // 每十分钟验证一下连接
        dataSource.setTimeBetweenEvictionRunsMillis(600000);
        // 运行ilde链接测试线程，剔除不可用的链接
        dataSource.setTestWhileIdle(true);
        dataSource.setMaxWait(-1);
        return dataSource;
    }


    public static DruidDataSource getDataSource(String tenantId) {
        return (DruidDataSource) DATASOURCE_MAP.get(tenantId);
    }

}
