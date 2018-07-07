package com.bosi.itms.shiro.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/1/15.
 */
//@Configuration
//@MapperScan(basePackages = "com.bosi.itms.shiro.dao",sqlSessionTemplateRef = "SqlSessionTemplateOne")
public class DataSourceOneConfig {

    @Value("classpath:mapper1/*.xml")
    private String mapperLocationPattern;

    @ConfigurationProperties(prefix = "spring.dataSource.druid.datasource1")
    @Bean(name = "datasource1")
    @Primary
    public DataSource dataSource1() throws SQLException {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "sessionFactory1")
    @Primary
    public SqlSessionFactory sqlSessionFactory1(@Qualifier(value = "datasource1") DataSource dataSource,
                                                PaginationInterceptor paginationInterceptor,
                                                @Qualifier(value = "globalConfiguration1") GlobalConfiguration globalConfiguration) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocationPattern));
        Interceptor[] interceptors = new Interceptor[]{paginationInterceptor};
        bean.setPlugins(interceptors);
        bean.setGlobalConfig(globalConfiguration);
        return bean.getObject();
    }

    @ConfigurationProperties(prefix = "globalConfig1")
    @Bean(name = "globalConfiguration1")
    public GlobalConfiguration globalConfiguration1() {
        return new GlobalConfiguration();
    }


    @Bean(name = "transactionManager1")
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager1(@Qualifier("datasource1") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "SqlSessionTemplateOne")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("sessionFactory1") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
