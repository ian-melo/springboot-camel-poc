package net.ism.sandbox.poc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {
	@Bean(name="mysql")
	public DataSource mysql(
			@Value("${mysql.datasource.url}") String url,
			@Value("${mysql.datasource.user}") String user,
			@Value("${mysql.datasource.pass}") String pass) {
	    return DataSourceBuilder.create()
	            .driverClassName("com.mysql.cj.jdbc.Driver")
	    		.url(url)
	    		.username(user)
	    		.password(pass).build();
	}
}
