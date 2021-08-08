package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Created by wwh
 * @date 2021/8/8.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        LOGGER.info("当前数据源为{}:", DataSourceContextHolder.getDB());
        return DataSourceContextHolder.getDB();
    }
}
