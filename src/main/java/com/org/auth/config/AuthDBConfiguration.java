package com.org.auth.config;

import com.org.auth.utils.AuthConstant;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.org.auth.repository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "platformTransactionManager")
public class AuthDBConfiguration {
    private final AuthDBConfig dbConfig;

    public AuthDBConfiguration(AuthDBConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Bean
    JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    PlatformTransactionManager platformTransactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dbConfig.dataSource());
        factoryBean.setPackagesToScan(new String [] {"com.org.auth.entity"});
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        factoryBean.setJpaProperties(jpaProperties());
        return factoryBean;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put(AuthConstant.DIALECT_KEY, AuthConstant.DIALECT_VALUE);
        properties.put("hibernate.dialect", "com.org.auth.config.DialectConfig");
        properties.put("spring.jpa.database-platform", "org.hibernate.dialect.MySQL8Dialect");
        properties.put(AuthConstant.SHOW_SQL_KEY, AuthConstant.SHOW_SQL_VALUE);
        properties.put(AuthConstant.FORMAT_SQL_KEY, AuthConstant.FORMAT_SQL_VALUE);
        properties.put("spring.jpa.hibernate.ddl-auto", "update");
        return properties;
    }
}
