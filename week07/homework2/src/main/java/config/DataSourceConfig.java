package config;

import config.enums.Datasources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Autowired
    private Environment env;

    @Bean(name = Datasources.MASTER)
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("master.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("master.datasource.url"));
        dataSource.setUsername(env.getProperty("master.datasource.username"));
        dataSource.setPassword(env.getProperty("master.datasource.password"));
        return dataSource;

    }

    @Bean(name = Datasources.SLAVE)
    public DataSource dataSourceSlave() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("slave.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("slave.datasource.url"));
        dataSource.setUsername(env.getProperty("slave.datasource.username"));
        dataSource.setPassword(env.getProperty("slave.datasource.password"));
        return dataSource;
    }
}