package net.ism.sandbox.poc.routes;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.ism.sandbox.poc.SampleConstants;

@SpringBootTest
public class AgendaCreateServiceTest extends CamelTestSupport {
	/* Valores para teste */
	private static final String FROM_AGENDA_CREATE = "direct:fromAgendaCreate";
	private static final String TO_AGENDA_HANDLER = "mock:toAgendaHandler";
	private static final String TO_CONTATO_UPDATE = "mock:toContatoUpdate";
	private static final String TO_CATALOGO_SAVE = "mock:toCatalogoSave";
	private static final String TO_AUTOR_LOG = "mock:toAutorLog";

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		return new AgendaCreateService(
				FROM_AGENDA_CREATE,
				TO_AGENDA_HANDLER,
				TO_CONTATO_UPDATE,
				TO_CATALOGO_SAVE,
				TO_AUTOR_LOG);
	}

	private String INVALID_SORT_XML() { return new StringBuilder()
			.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
			.append("<agenda>")
				.append("<autor>")
					.append("<id>1020</id>")
					.append("<email>marcos.ribas@email.com</email>")
					.append("<nome>Marcos Ribas</nome>")
					.append("<telefone>+55 19 932909285</telefone>")
				.append("</autor>")
				.append("<dataCriacao>2020-08-06T13:26:48.233-03:00</dataCriacao>")
				.append("<visibilidade>PUBLICO</visibilidade>")
				.append("<contatos>")
					.append("<contato>")
						.append("<id>1020</id>")
						.append("<email>marcos.ribas@email.com</email>")
						.append("<nome>Marcos Ribas</nome>")
						.append("<telefone>+55 19 932909285</telefone>")
					.append("</contato>")
					.append("<contato>")
						.append("<id>1020</id>")
						.append("<email>marcos.ribas@email.com</email>")
						.append("<nome>Marcos Ribas</nome>")
						.append("<telefone>+55 19 932909285</telefone>")
					.append("</contato>")
				.append("</contatos>")
			.append("</agenda>")
			.toString();
	}

	private String INVALID_MISSING_XML() { return new StringBuilder()
			.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
			.append("<agenda>")
				.append("<dataCriacao>2020-08-06T13:26:48.233-03:00</dataCriacao>")
				.append("<visibilidade>PRIVADO</visibilidade>")
				.append("<contatos>")
					.append("<contato>")
						.append("<id>1020</id>")
						.append("<nome>Marcos Ribas</nome>")
						.append("<email>marcos.ribas@email.com</email>")
						.append("<telefone>+55 19 932909285</telefone>")
					.append("</contato>")
					.append("<contato>")
						.append("<id>1020</id>")
						.append("<nome>Marcos Ribas</nome>")
						.append("<email>marcos.ribas@email.com</email>")
						.append("<telefone>+55 19 932909285</telefone>")
					.append("</contato>")
				.append("</contatos>")
			.append("</agenda>")
			.toString();
	}

	private String INVALID_ADDITIONAL_XML() { return new StringBuilder()
			.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
			.append("<agenda>")
				.append("<autor>")
					.append("<id>1020</id>")
					.append("<nome>Marcos Ribas</nome>")
					.append("<email>marcos.ribas@email.com</email>")
					.append("<telefone>+55 19 932909285</telefone>")
				.append("</autor>")
				.append("<dataCriacao>2020-08-06T13:26:48.233-03:00</dataCriacao>")
				.append("<visibilidade>PUBLICO</visibilidade>")
				.append("<contatos>")
					.append("<contato>")
						.append("<id>1020</id>")
						.append("<nome>Marcos Ribas</nome>")
						.append("<email>marcos.ribas@email.com</email>")
						.append("<telefone>+55 19 932909285</telefone>")
					.append("</contato>")
					.append("<contato>")
						.append("<id>1020</id>")
						.append("<nome>Marcos Ribas</nome>")
						.append("<email>marcos.ribas@email.com</email>")
						.append("<telefone>+55 19 932909285</telefone>")
					.append("</contato>")
				.append("</contatos>")
			.append("</agenda>")
			.append("<qualquerCoisa/>")
			.toString();
	}

	private String INVALID_VALUE_XML() { return new StringBuilder()
			.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
			.append("<agenda>")
				.append("<autor>")
					.append("<id>1020</id>")
					.append("<nome>Marcos Ribas</nome>")
					.append("<email>marcos.ribas@email.com</email>")
					.append("<telefone>+55 19 932909285</telefone>")
				.append("</autor>")
				.append("<dataCriacao>2020-08-06T13:26:48.233-03:00</dataCriacao>")
				.append("<visibilidade>pessoal</visibilidade>")
				.append("<contatos>")
					.append("<contato>")
						.append("<id>1020</id>")
						.append("<nome>Marcos Ribas</nome>")
						.append("<email>marcos.ribas@email.com</email>")
						.append("<telefone>+55 19 932909285</telefone>")
					.append("</contato>")
					.append("<contato>")
						.append("<id>1020</id>")
						.append("<nome>Marcos Ribas</nome>")
						.append("<email>marcos.ribas@email.com</email>")
						.append("<telefone>+55 19 932909285</telefone>")
					.append("</contato>")
				.append("</contatos>")
			.append("</agenda>")
			.toString();
	}

	@Test
	public void when_fromAgendaCreate_given_validInputPublicoAllInfo_then_pass() throws Exception {
		getMockEndpoint(TO_AGENDA_HANDLER).expectedBodiesReceived();
		getMockEndpoint(TO_CONTATO_UPDATE).expectedBodiesReceived(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		getMockEndpoint(TO_CATALOGO_SAVE).expectedBodiesReceived(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		getMockEndpoint(TO_AUTOR_LOG).expectedBodiesReceived(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		template.sendBody(FROM_AGENDA_CREATE, SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromAgendaCreate_given_validInputPrivadoSomeInfo_then_pass() throws Exception {
		getMockEndpoint(TO_AGENDA_HANDLER).expectedBodiesReceived();
		getMockEndpoint(TO_CONTATO_UPDATE).expectedBodiesReceived(SampleConstants.VALID_SOMEINFO_PRIVADO_AGENDA_XML());
		getMockEndpoint(TO_CATALOGO_SAVE).expectedBodiesReceived(SampleConstants.VALID_SOMEINFO_PRIVADO_AGENDA_XML());
		getMockEndpoint(TO_AUTOR_LOG).expectedBodiesReceived(SampleConstants.VALID_SOMEINFO_PRIVADO_AGENDA_XML());
		template.sendBody(FROM_AGENDA_CREATE, SampleConstants.VALID_SOMEINFO_PRIVADO_AGENDA_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromAgendaCreate_given_validInputRestritoOnlyInfo_then_pass() throws Exception {
		getMockEndpoint(TO_AGENDA_HANDLER).expectedBodiesReceived();
		getMockEndpoint(TO_CONTATO_UPDATE).expectedBodiesReceived(SampleConstants.VALID_ONLYINFO_RESTRITO_AGENDA_XML());
		getMockEndpoint(TO_CATALOGO_SAVE).expectedBodiesReceived(SampleConstants.VALID_ONLYINFO_RESTRITO_AGENDA_XML());
		getMockEndpoint(TO_AUTOR_LOG).expectedBodiesReceived(SampleConstants.VALID_ONLYINFO_RESTRITO_AGENDA_XML());
		template.sendBody(FROM_AGENDA_CREATE, SampleConstants.VALID_ONLYINFO_RESTRITO_AGENDA_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromAgendaCreate_given_invalidSortInput_then_filter() throws Exception {
		getMockEndpoint(TO_AGENDA_HANDLER).expectedBodiesReceived(INVALID_SORT_XML());
		getMockEndpoint(TO_CONTATO_UPDATE).expectedBodiesReceived();
		getMockEndpoint(TO_CATALOGO_SAVE).expectedBodiesReceived();
		getMockEndpoint(TO_AUTOR_LOG).expectedBodiesReceived();
		template.sendBody(FROM_AGENDA_CREATE, INVALID_SORT_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromAgendaCreate_given_invalidMissingInput_then_filter() throws Exception {
		getMockEndpoint(TO_AGENDA_HANDLER).expectedBodiesReceived(INVALID_MISSING_XML());
		getMockEndpoint(TO_CONTATO_UPDATE).expectedBodiesReceived();
		getMockEndpoint(TO_CATALOGO_SAVE).expectedBodiesReceived();
		getMockEndpoint(TO_AUTOR_LOG).expectedBodiesReceived();
		template.sendBody(FROM_AGENDA_CREATE, INVALID_MISSING_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromAgendaCreate_given_invalidAdditionalInput_then_filter() throws Exception {
		getMockEndpoint(TO_AGENDA_HANDLER).expectedBodiesReceived(INVALID_ADDITIONAL_XML());
		getMockEndpoint(TO_CONTATO_UPDATE).expectedBodiesReceived();
		getMockEndpoint(TO_CATALOGO_SAVE).expectedBodiesReceived();
		getMockEndpoint(TO_AUTOR_LOG).expectedBodiesReceived();
		template.sendBody(FROM_AGENDA_CREATE, INVALID_ADDITIONAL_XML());
		assertMockEndpointsSatisfied();
	}

	@Test
	public void when_fromAgendaCreate_given_invalidValueInput_then_filter() throws Exception {
		getMockEndpoint(TO_AGENDA_HANDLER).expectedBodiesReceived(INVALID_VALUE_XML());
		getMockEndpoint(TO_CONTATO_UPDATE).expectedBodiesReceived();
		getMockEndpoint(TO_CATALOGO_SAVE).expectedBodiesReceived();
		getMockEndpoint(TO_AUTOR_LOG).expectedBodiesReceived();
		template.sendBody(FROM_AGENDA_CREATE, INVALID_VALUE_XML());
		assertMockEndpointsSatisfied();
	}

}
