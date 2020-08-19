package net.ism.sandbox.poc.routes;

import static org.junit.Assert.assertEquals;

import java.util.Map;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.ism.sandbox.poc.SampleConstants;

@ContextConfiguration
public class ContatoUpdateSubserviceTest extends AbstractJUnit4SpringContextTests {
	private ObjectMapper jsonMapper = new ObjectMapper();
	/* Valores para teste */
	private static final String BASE_URL = "http://localhost:4444";
	private static final String FROM_UPDATE = "direct:fromUpdate";
	private static final String TO_UPDATE_AUTOR = "mock:toUpdateAutor";
	private static final String TO_UPDATE_CONTATO = "mock:toUpdateContato";
	private static final String TO_UPDATE_CONVERT = "mock:toUpdateConvert";
	private static final String TO_UPDATE_REST = "mock:toUpdateRest";
	private static final String TO_HTTP_REST = "mock:toHttpRest";
	/* Valores iguais na rota */
	private static final String FROM_UPDATE_AUTOR = "direct:subrota-perfil-update-autor";
	private static final String FROM_UPDATE_CONTATO = "direct:subrota-perfil-update-contato";
	private static final String FROM_UPDATE_CONVERT = "direct:subrota-perfil-update-convert";
	private static final String FROM_UPDATE_REST = "direct:subrota-perfil-update-rest";
	/* Configuração */
	@Configuration
    public static class ContextConfig extends SingleRouteCamelConfiguration {
        @Bean
        public RouteBuilder route() {
        	return new ContatoUpdateSubservice(
    				BASE_URL,
    				FROM_UPDATE,
    				TO_UPDATE_AUTOR,
    				TO_UPDATE_CONTATO,
    				TO_UPDATE_CONVERT,
    				TO_UPDATE_REST,
    				TO_HTTP_REST);
        }
    }
	@Produce(FROM_UPDATE)
	protected ProducerTemplate fromUpdateTemplate;
	@EndpointInject(TO_UPDATE_AUTOR)
	protected MockEndpoint toUpdateAutorEndpoint;
	@EndpointInject(TO_UPDATE_CONTATO)
	protected MockEndpoint toUpdateContatoEndpoint;
	@EndpointInject(TO_UPDATE_CONVERT)
	protected MockEndpoint toUpdateConvertEndpoint;
	@EndpointInject(TO_UPDATE_REST)
	protected MockEndpoint toUpdateRestEndpoint;
	@EndpointInject(TO_HTTP_REST)
	protected MockEndpoint toHttpRestEndpoint;
	@Produce(FROM_UPDATE_AUTOR)
	protected ProducerTemplate fromUpdateAutorTemplate;
	@Produce(FROM_UPDATE_CONTATO)
	protected ProducerTemplate fromUpdateContatoTemplate;
	@Produce(FROM_UPDATE_CONVERT)
	protected ProducerTemplate fromUpdateConvertTemplate;
	@Produce(FROM_UPDATE_REST)
	protected ProducerTemplate fromUpdateRestTemplate;

	/* Testes */
	@DirtiesContext
    @Test
	public void when_fromUpdate_then_multicast() throws Exception {
		toUpdateAutorEndpoint.expectedBodiesReceived(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		toUpdateContatoEndpoint.expectedBodiesReceived(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		fromUpdateTemplate.sendBody(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		toUpdateAutorEndpoint.assertIsSatisfied();
        toUpdateContatoEndpoint.assertIsSatisfied();
	}

	@DirtiesContext
    @Test
	public void when_fromUpdateAutor_then_transformedBody() throws Exception {
		toUpdateConvertEndpoint.expectedBodiesReceived(SampleConstants.VALID_ALLINFO_AGENDA_PERFIL_XML());
		fromUpdateAutorTemplate.sendBody(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		toUpdateConvertEndpoint.assertIsSatisfied();
	}

	@DirtiesContext
    @Test
	public void when_fromUpdateContato_then_transformedBody() throws Exception {
		toUpdateConvertEndpoint.expectedBodiesReceived(SampleConstants.VALID_ALLINFO_AGENDA_PERFIL_XML(), SampleConstants.VALID_ALLINFO_AGENDA_PERFIL_XML());
		fromUpdateContatoTemplate.sendBody(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		toUpdateConvertEndpoint.assertIsSatisfied();
	}

	@DirtiesContext
    @Test
	public void when_fromUpdateConvert_given_allInfo_then_marshal() throws Exception {
		toUpdateRestEndpoint.expectedMessageCount(1);

		fromUpdateConvertTemplate.sendBody(SampleConstants.VALID_ALLINFO_AGENDA_PERFIL_XML());
		Map<String,Object> responseBody = jsonMapper.readValue(((byte[]) toUpdateRestEndpoint.getExchanges().get(0).getIn().getBody()),
				new TypeReference<Map<String,Object>>(){});

		toUpdateRestEndpoint.assertIsSatisfied();
		assertEquals(SampleConstants.VALID_ALLINFO_AGENDA_PERFIL_MAP(), responseBody);
	}

	@DirtiesContext
    @Test
	public void when_fromUpdateConvert_given_someInfo_then_marshal() throws Exception {
		toUpdateRestEndpoint.expectedMessageCount(1);

		fromUpdateConvertTemplate.sendBody(SampleConstants.VALID_SOMEINFO_AGENDA_PERFIL_XML());
		Map<String,Object> responseBody = jsonMapper.readValue(((byte[]) toUpdateRestEndpoint.getExchanges().get(0).getIn().getBody()),
				new TypeReference<Map<String,Object>>(){});

		toUpdateRestEndpoint.assertIsSatisfied();
		assertEquals(SampleConstants.VALID_SOMEINFO_AGENDA_PERFIL_MAP(), responseBody);
	}

	@DirtiesContext
    @Test
	public void when_fromUpdateConvert_given_onlyInfo_then_filter() throws Exception {
		toUpdateRestEndpoint.expectedMessageCount(0);
		fromUpdateConvertTemplate.sendBody(SampleConstants.VALID_ONLYINFO_AGENDA_PERFIL_XML());
		toUpdateRestEndpoint.assertIsSatisfied();
	}

}

