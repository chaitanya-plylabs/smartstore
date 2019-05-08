package com.jio.iot;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.messaging.unitofwork.RollbackConfigurationType;
import org.axonframework.serialization.Serializer;
import org.axonframework.springboot.util.jpa.ContainerManagedEntityManagerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class AppConfig {
    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        final var dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(this.environment.getProperty("datasource.driver"));
        dataSourceConfig.setJdbcUrl(this.environment.getProperty("datasource.url"));
        dataSourceConfig.setUsername(this.environment.getProperty("datasource.username"));
        dataSourceConfig.setPassword(this.environment.getProperty("datasource.password"));
        return new HikariDataSource(dataSourceConfig);
    }

    @Bean
    SimpleCommandBus commandBus(final TransactionManager transactionManager) {
        return SimpleCommandBus.builder()
                .rollbackConfiguration(RollbackConfigurationType.NEVER)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public EntityManagerProvider entityManagerProvider() {
        return new ContainerManagedEntityManagerProvider();
    }

    @Bean
    public TokenStore tokenStore(final Serializer serializer, final EntityManagerProvider entityManagerProvider) {
        return JpaTokenStore.builder()
                .entityManagerProvider(entityManagerProvider)
                .serializer(serializer)
                .build();
    }
}
