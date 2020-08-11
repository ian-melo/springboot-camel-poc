package net.ism.sandbox.springbootcamel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
//	@Autowired
//	private CamelContext context;
//	@Autowired
//	private MysqlConnectionPoolDataSource mysqlPool;
//	@Value("${activemq.base.url}")
//	private String activeMqAddress;
//
//	@PostConstruct
//	public void init() throws Exception {
//		context.addComponent("activemq", ActiveMQComponent.activeMQComponent(activeMqAddress));
//	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
