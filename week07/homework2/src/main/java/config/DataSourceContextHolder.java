package config;

import config.enums.Datasources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Created by diandian
 * @date 2021/8/8.
 */
public class DataSourceContextHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceContextHolder.class);
    /**
     * 默认数据源
     */
    public static final String DEFAULT_DATASOURCE = Datasources.MASTER;

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    // 设置数据源名
    public static void setDB(String dbType) {
        LOGGER.info("切换到{}数据源", dbType);
        contextHolder.set(dbType);
    }

    // 获取数据源名
    public static String getDB() {
        return (contextHolder.get());
    }

    // 清除数据源名
    public static void clearDB() {
        contextHolder.remove();
    }
}
