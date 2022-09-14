package com.marsh.sqlapi.helper;

public class RoutingDataSourceContext  {

    public static final ThreadLocal<String> RouteKey = new ThreadLocal<>();

    public static String getMasterKey() {
        return "main";
    }

    /**
     * 获取主数据库的key
     * @return
     */
    public static String getPgMainKey() {
        return "pgMain";
    }

    public static String getMysqlMainKey() {
        return "mysqlMain";
    }

    /**
     * 获取数据库key
     * @return
     */
    public static String getRouteKey() {
        return  RouteKey.get();
    }

    /**
     * 设置数据库的key
     * @param key
     */
    public static void setRouteKey(String key) {
        RouteKey.set(key);
    }

}
