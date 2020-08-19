package net.ism.sandbox.poc.routes;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import net.ism.sandbox.poc.SampleConstants;

@ContextConfiguration
public class AutorLogSubserviceTest extends AbstractJUnit4SpringContextTests {
	/* Valores para teste */
	private static final String FROM_LOG = "direct:fromLog";
	private static final String TO_MYSQL = "mock:toMysql";
	/* Configuração */
	@Configuration
    public static class ContextConfig extends SingleRouteCamelConfiguration {
        @Bean
        public RouteBuilder route() {
    		return new AutorLogSubservice(
    				FROM_LOG,
    				TO_MYSQL);
        }
    }
	@EndpointInject(TO_MYSQL)
    protected MockEndpoint toMysqlEndpoint;
    @Produce(FROM_LOG)
    protected ProducerTemplate fromLogTemplate;

    /* Testes */
    @DirtiesContext
    @Test
    public void when_fromLog_then_insert() throws Exception {
        final String query = "INSERT INTO registro(data, registro) VALUES ('2020-08-06T13:26:48.233-03:00', '[Autor=1020, Visibilidade=PUBLICO]')";
		toMysqlEndpoint.expectedBodiesReceived(query);
		fromLogTemplate.sendBody(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		toMysqlEndpoint.assertIsSatisfied();
    }
}
