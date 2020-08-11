package net.ism.sandbox.springbootcamel.config;

import java.util.UUID;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

@Configuration
public class CamelConfig {

	@Bean
	public CamelContext getContext(SimpleRegistry registry, ActiveMQComponent activemq) {
		CamelContext context = new DefaultCamelContext(registry);
		context.setUuidGenerator(() -> UUID.randomUUID().toString());
		context.addComponent("activemq", activemq);
		return context;
	}

	@Bean
	public ActiveMQComponent component(@Value("${activemq.base.url}") String activeMqAddress) {
		return ActiveMQComponent.activeMQComponent(activeMqAddress);
	}

	@Bean
	public SimpleRegistry registry(MysqlConnectionPoolDataSource dataSource) {
		SimpleRegistry registry = new SimpleRegistry();
		registry.put("mysql", dataSource);
		return registry;
	}

	@Bean
	public MysqlConnectionPoolDataSource getDataSource(
			@Value("${mysql.datasource.host}") String host,
			@Value("${mysql.datasource.port}") int port,
			@Value("${mysql.datasource.db}") String db,
			@Value("${mysql.datasource.user}") String user,
			@Value("${mysql.datasource.pass}") String pass) {
		MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
		dataSource.setDatabaseName(db);
		dataSource.setServerName(host);
		dataSource.setPort(port);
		dataSource.setUser(user);
		dataSource.setPassword(pass);
		return dataSource;
	}
}
