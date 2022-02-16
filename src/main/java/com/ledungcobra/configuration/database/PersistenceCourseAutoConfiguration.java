package com.ledungcobra.configuration.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

//@Configuration
//@EnableJpaRepositories(
//        basePackages = "com.ledungcobra.course",
//        entityManagerFactoryRef = "courseEntityManager",
//        transactionManagerRef = "courseTransactionManager"
//)
public class PersistenceCourseAutoConfiguration {

    @Autowired
    private Environment env;

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource courseDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean(name = "courseEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean courseEntityManager() {
        var em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(courseDataSource());
        em.setPackagesToScan(
                "com.ledungcobra.course.entity");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));

        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    public PlatformTransactionManager courseTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                courseEntityManager().getObject());
        return transactionManager;
    }
}
