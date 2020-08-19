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
public class AgendaCreateServiceTest extends AbstractJUnit4SpringContextTests {
	/* Valores para teste */
	private static final String FROM_AGENDA_CREATE = "direct:fromAgendaCreate";
	private static final String TO_AGENDA_HANDLER = "mock:toAgendaHandler";
	private static final String TO_CONTATO_UPDATE = "mock:toContatoUpdate";
	private static final String TO_CATALOGO_SAVE = "mock:toCatalogoSave";
	private static final String TO_AUTOR_LOG = "mock:toAutorLog";
	/* Configuração */
	@Configuration
    public static class ContextConfig extends SingleRouteCamelConfiguration {
        @Bean
        public RouteBuilder route() {
        	return new AgendaCreateService(
    				FROM_AGENDA_CREATE,
    				TO_AGENDA_HANDLER,
    				TO_CONTATO_UPDATE,
    				TO_CATALOGO_SAVE,
    				TO_AUTOR_LOG);
        }
    }
	@Produce(FROM_AGENDA_CREATE)
	protected ProducerTemplate fromAgendaCreateTemplate;
	@EndpointInject(TO_AGENDA_HANDLER)
	protected MockEndpoint toAgendaHandlerEndpoint;
	@EndpointInject(TO_CONTATO_UPDATE)
	protected MockEndpoint toContatoUpdateEndpoint;
	@EndpointInject(TO_CATALOGO_SAVE)
	protected MockEndpoint toCatalogoSaveEndpoint;
	@EndpointInject(TO_AUTOR_LOG)
	protected MockEndpoint toAutorLogEndpoint;

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

	/* Testes */
	@DirtiesContext
    @Test
	public void when_fromAgendaCreate_given_validInputPublicoAllInfo_then_pass() throws Exception {
		toAgendaHandlerEndpoint.expectedBodiesReceived();
		toContatoUpdateEndpoint.expectedBodiesReceived(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		toCatalogoSaveEndpoint.expectedBodiesReceived(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		toAutorLogEndpoint.expectedBodiesReceived(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		fromAgendaCreateTemplate.sendBody(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
		toAgendaHandlerEndpoint.assertIsSatisfied();
		toContatoUpdateEndpoint.assertIsSatisfied();
		toCatalogoSaveEndpoint.assertIsSatisfied();
		toAutorLogEndpoint.assertIsSatisfied();
	}

    @DirtiesContext
    @Test
	public void when_fromAgendaCreate_given_validInputPrivadoSomeInfo_then_pass() throws Exception {
    	toAgendaHandlerEndpoint.expectedBodiesReceived();
    	toContatoUpdateEndpoint.expectedBodiesReceived(SampleConstants.VALID_SOMEINFO_PRIVADO_AGENDA_XML());
    	toCatalogoSaveEndpoint.expectedBodiesReceived(SampleConstants.VALID_SOMEINFO_PRIVADO_AGENDA_XML());
    	toAutorLogEndpoint.expectedBodiesReceived(SampleConstants.VALID_SOMEINFO_PRIVADO_AGENDA_XML());
    	fromAgendaCreateTemplate.sendBody(SampleConstants.VALID_SOMEINFO_PRIVADO_AGENDA_XML());
    	toAgendaHandlerEndpoint.assertIsSatisfied();
    	toContatoUpdateEndpoint.assertIsSatisfied();
    	toCatalogoSaveEndpoint.assertIsSatisfied();
    	toAutorLogEndpoint.assertIsSatisfied();
	}

	@DirtiesContext
    @Test
	public void when_fromAgendaCreate_given_validInputRestritoOnlyInfo_then_pass() throws Exception {
		toAgendaHandlerEndpoint.expectedBodiesReceived();
		toContatoUpdateEndpoint.expectedBodiesReceived(SampleConstants.VALID_ONLYINFO_RESTRITO_AGENDA_XML());
		toCatalogoSaveEndpoint.expectedBodiesReceived(SampleConstants.VALID_ONLYINFO_RESTRITO_AGENDA_XML());
		toAutorLogEndpoint.expectedBodiesReceived(SampleConstants.VALID_ONLYINFO_RESTRITO_AGENDA_XML());
		fromAgendaCreateTemplate.sendBody(SampleConstants.VALID_ONLYINFO_RESTRITO_AGENDA_XML());
		toAgendaHandlerEndpoint.assertIsSatisfied();
		toContatoUpdateEndpoint.assertIsSatisfied();
		toCatalogoSaveEndpoint.assertIsSatisfied();
		toAutorLogEndpoint.assertIsSatisfied();
	}

	@DirtiesContext
    @Test
	public void when_fromAgendaCreate_given_invalidSortInput_then_filter() throws Exception {
		toAgendaHandlerEndpoint.expectedBodiesReceived(INVALID_SORT_XML());
		toContatoUpdateEndpoint.expectedBodiesReceived();
		toCatalogoSaveEndpoint.expectedBodiesReceived();
		toAutorLogEndpoint.expectedBodiesReceived();
		fromAgendaCreateTemplate.sendBody(INVALID_SORT_XML());
		toAgendaHandlerEndpoint.assertIsSatisfied();
		toContatoUpdateEndpoint.assertIsSatisfied();
		toCatalogoSaveEndpoint.assertIsSatisfied();
		toAutorLogEndpoint.assertIsSatisfied();
	}

	@DirtiesContext
    @Test
	public void when_fromAgendaCreate_given_invalidMissingInput_then_filter() throws Exception {
		toAgendaHandlerEndpoint.expectedBodiesReceived(INVALID_MISSING_XML());
		toContatoUpdateEndpoint.expectedBodiesReceived();
		toCatalogoSaveEndpoint.expectedBodiesReceived();
		toAutorLogEndpoint.expectedBodiesReceived();
		fromAgendaCreateTemplate.sendBody(INVALID_MISSING_XML());
		toAgendaHandlerEndpoint.assertIsSatisfied();
		toContatoUpdateEndpoint.assertIsSatisfied();
		toCatalogoSaveEndpoint.assertIsSatisfied();
		toAutorLogEndpoint.assertIsSatisfied();
	}

	@DirtiesContext
    @Test
	public void when_fromAgendaCreate_given_invalidAdditionalInput_then_filter() throws Exception {
		toAgendaHandlerEndpoint.expectedBodiesReceived(INVALID_ADDITIONAL_XML());
		toContatoUpdateEndpoint.expectedBodiesReceived();
		toCatalogoSaveEndpoint.expectedBodiesReceived();
		toAutorLogEndpoint.expectedBodiesReceived();
		fromAgendaCreateTemplate.sendBody(INVALID_ADDITIONAL_XML());
		toAgendaHandlerEndpoint.assertIsSatisfied();
		toContatoUpdateEndpoint.assertIsSatisfied();
		toCatalogoSaveEndpoint.assertIsSatisfied();
		toAutorLogEndpoint.assertIsSatisfied();
	}

	@DirtiesContext
    @Test
	public void when_fromAgendaCreate_given_invalidValueInput_then_filter() throws Exception {
		toAgendaHandlerEndpoint.expectedBodiesReceived(INVALID_VALUE_XML());
		toContatoUpdateEndpoint.expectedBodiesReceived();
		toCatalogoSaveEndpoint.expectedBodiesReceived();
		toAutorLogEndpoint.expectedBodiesReceived();
		fromAgendaCreateTemplate.sendBody(INVALID_VALUE_XML());
		toAgendaHandlerEndpoint.assertIsSatisfied();
		toContatoUpdateEndpoint.assertIsSatisfied();
		toCatalogoSaveEndpoint.assertIsSatisfied();
		toAutorLogEndpoint.assertIsSatisfied();
	}

}
