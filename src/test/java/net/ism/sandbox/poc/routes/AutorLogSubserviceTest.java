package net.ism.sandbox.poc.routes;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import net.ism.sandbox.poc.SampleConstants;

public class AutorLogSubserviceTest extends CamelTestSupport {
	/* Valores para teste */
	private static final String FROM_LOG = "direct:fromLog";
	private static final String TO_MYSQL = "mock:toMysql";

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		return new AutorLogSubservice(
				FROM_LOG,
				TO_MYSQL);
	}

	@Test
	public void when_fromLog_then_insert() throws Exception {
		final String query = "INSERT INTO registro(data, registro) VALUES ('2020-08-06T13:26:48.233-03:00', '[Autor=1020, Visibilidade=PUBLICO]')";
		getMockEndpoint(TO_MYSQL).expectedBodiesReceived(query);
		template.sendBody(FROM_LOG, SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		assertMockEndpointsSatisfied();
	}

}
