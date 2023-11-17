package com.zph.programmer.springdemo.testconf;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.unitils.database.UnitilsDataSourceFactoryBean;

import javax.sql.DataSource;

@PropertySource("classpath:unitils.properties")
@TestConfiguration
public class TestConf {
    DataSource dataSource = (DataSource) new UnitilsDataSourceFactoryBean().getObject();

    public TestConf() throws Exception {
    }

    @Bean(name = "dataSource")
    public DataSource dbunitDataSource() throws Exception {
        return dataSource;
    }
}
