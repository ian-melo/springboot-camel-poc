package net.ism.sandbox.poc.routes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.ism.sandbox.poc.SampleConstants;

@SpringBootTest
public class ContatoUpdateSubserviceTest extends CamelTestSupport {
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
//	private static final String FROM_UPDATE_REST = "direct:subrota-perfil-update-rest";

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		return new ContatoUpdateSubservice(
				BASE_URL,
				FROM_UPDATE,
				TO_UPDATE_AUTOR,
				TO_UPDATE_CONTATO,
				TO_UPDATE_CONVERT,
				TO_UPDATE_REST,
				TO_HTTP_REST);
	}

	@Test
	public void when_fromUpdate_then_multicast() throws Exception {
		getMockEndpoint(TO_UPDATE_AUTOR).expectedBodiesReceived(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		getMockEndpoint(TO_UPDATE_CONTATO).expectedBodiesReceived(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		template.sendBody(FROM_UPDATE, SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromUpdateAutor_then_transformedBody() throws Exception {
		getMockEndpoint(TO_UPDATE_CONVERT).expectedBodiesReceived(SampleConstants.VALID_ALLINFO_AGENDA_PERFIL_XML());
		template.sendBody(FROM_UPDATE_AUTOR, SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromUpdateContato_then_transformedBody() throws Exception {
		getMockEndpoint(TO_UPDATE_CONVERT).expectedBodiesReceived(SampleConstants.VALID_ALLINFO_AGENDA_PERFIL_XML(), SampleConstants.VALID_ALLINFO_AGENDA_PERFIL_XML());
		template.sendBody(FROM_UPDATE_CONTATO, SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromUpdateConvert_given_allInfo_then_marshal() throws Exception {
		MockEndpoint toUpdateRest = getMockEndpoint(TO_UPDATE_REST);
		toUpdateRest.expectedMessageCount(1);

		template.sendBody(FROM_UPDATE_CONVERT, SampleConstants.VALID_ALLINFO_AGENDA_PERFIL_XML());
		Map<String,Object> responseBody = jsonMapper.readValue(((byte[]) toUpdateRest.getExchanges().get(0).getIn().getBody()),
				new TypeReference<Map<String,Object>>(){});

		assertMockEndpointsSatisfied();
		assertEquals(SampleConstants.VALID_ALLINFO_AGENDA_PERFIL_MAP(), responseBody);
	}

	@Test
	public void when_fromUpdateConvert_given_someInfo_then_marshal() throws Exception {
		MockEndpoint toUpdateRest = getMockEndpoint(TO_UPDATE_REST);
		toUpdateRest.expectedMessageCount(1);

		template.sendBody(FROM_UPDATE_CONVERT, SampleConstants.VALID_SOMEINFO_AGENDA_PERFIL_XML());
		Map<String,Object> responseBody = jsonMapper.readValue(((byte[]) toUpdateRest.getExchanges().get(0).getIn().getBody()),
				new TypeReference<Map<String,Object>>(){});

		assertMockEndpointsSatisfied();
		assertEquals(SampleConstants.VALID_SOMEINFO_AGENDA_PERFIL_MAP(), responseBody);
	}

	@Test
	public void when_fromUpdateConvert_given_onlyInfo_then_filter() throws Exception {
		getMockEndpoint(TO_UPDATE_REST).expectedMessageCount(0);
		template.sendBody(FROM_UPDATE_CONVERT, SampleConstants.VALID_ONLYINFO_AGENDA_PERFIL_XML());
		assertMockEndpointsSatisfied();
	}

}

