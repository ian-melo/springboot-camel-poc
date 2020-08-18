package net.ism.sandbox.poc.routes;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.ism.sandbox.poc.SampleConstants;

@SpringBootTest
public class CatalogoSaveSubserviceTest extends CamelTestSupport {
	/* Valores para teste */
	private static final String BASE_URL = "http://localhost:4444";
	private static final String FROM_SAVE = "direct:fromSave";
	private static final String TO_SAVE_CONVERT = "mock:toSaveConvert";
	private static final String TO_SAVE_SOAP = "mock:toSaveSoap";
	private static final String TO_HTTP_SOAP = "mock:toHttpSoap";
	/* Valores iguais na rota */
	private static final String FROM_SAVE_CONVERT = "direct:subrota-catalogo-save-convert";
//	private static final String FROM_SAVE_SOAP = "direct:subrota-catalogo-save-soap";

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		return new CatalogoSaveSubservice(
				BASE_URL,
				FROM_SAVE,
				TO_SAVE_CONVERT,
				TO_SAVE_SOAP,
				TO_HTTP_SOAP);
	}

	@Test
	public void when_fromSave_given_publico_then_pass() throws Exception {
		getMockEndpoint(TO_SAVE_CONVERT).expectedBodiesReceived(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		template.sendBody(FROM_SAVE, SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromSave_given_privado_then_filter() throws Exception {
		getMockEndpoint(TO_SAVE_CONVERT).expectedMessageCount(0);
		template.sendBody(FROM_SAVE, SampleConstants.VALID_ALLINFO_PRIVADO_AGENDA_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromSaveConvert_given_allInfo_then_convert() throws Exception {
		getMockEndpoint(TO_SAVE_SOAP).expectedBodiesReceived(SampleConstants.VALID_ALLINFO_CATALOGO_XML());
		template.sendBody(FROM_SAVE_CONVERT, SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromSaveConvert_given_someInfo_then_convert() throws Exception {
		getMockEndpoint(TO_SAVE_SOAP).expectedBodiesReceived(SampleConstants.VALID_SOMEINFO_CATALOGO_XML());
		template.sendBody(FROM_SAVE_CONVERT, SampleConstants.VALID_SOMEINFO_PUBLICO_AGENDA_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromSaveConvert_given_onlyInfo_then_convert() throws Exception {
		getMockEndpoint(TO_SAVE_SOAP).expectedBodiesReceived(SampleConstants.VALID_ONLYINFO_CATALOGO_XML());
		template.sendBody(FROM_SAVE_CONVERT, SampleConstants.VALID_ONLYINFO_PUBLICO_AGENDA_XML());
		assertMockEndpointsSatisfied();
	}

}
