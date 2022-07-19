package com.jbignacio.saga.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import static io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@SuppressWarnings("deprecation")
@Configuration
public class DataSourceConfig extends AbstractR2dbcConfiguration  {

	@Override
	public ConnectionFactory connectionFactory() {
		return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(DRIVER, "postgresql")
                        .option(HOST, "localhost")
                        .option(PORT, 5432)
                        .option(USER, "postgres")
                        .option(PASSWORD, "1234")
                        .option(DATABASE, "saga_choreography")
                        .option(MAX_SIZE, 40)
                        .build());
	}


	@Bean
	public DatabaseClient client() {
		return DatabaseClient.create(connectionFactory());
	}

	@Bean
	public R2dbcEntityTemplate r2dbcEntityTemplate() {
		return new R2dbcEntityTemplate(connectionFactory());
	}

}
