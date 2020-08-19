package net.ism.sandbox.poc.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AgendaCreateService extends RouteBuilder {
	private String fromAgendaCreate;
	private String toAgendaHandler;
	private String toContatoUpdate;
	private String toCatalogoSave;
	private String toAutorLog;

	@Autowired
	public AgendaCreateService(
			@Value("${route.AgendaCreateService}") String fromAgendaCreate,
			@Value("${route.AgendaCreateService.handler}") String toAgendaHandler,
			@Value("${route.ContatoUpdateSubservice}") String toContatoUpdate,
			@Value("${route.CatalogoSaveSubservice}") String toCatalogoSave,
			@Value("${route.AutorLogSubservice}") String toAutorLog) {
		super();
		this.fromAgendaCreate = fromAgendaCreate;
		this.toAgendaHandler = toAgendaHandler;
		this.toContatoUpdate = toContatoUpdate;
		this.toCatalogoSave = toCatalogoSave;
		this.toAutorLog = toAutorLog;
	}

	@Override
	public void configure() throws Exception {
		errorHandler(
				deadLetterChannel(this.toAgendaHandler).
				useOriginalMessage().
				maximumRedeliveries(3).
				redeliveryDelay(1000).
				onRedelivery((exchange) -> {
					int it = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER);
					int n = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER);
					log.warn("[{}] Conteúdo da mensagem inválido. Enviando para fila de erro (tentativa {} de {}).",
							simple("${id}").evaluate(exchange, String.class), it, n);
				}));

		from(this.fromAgendaCreate).
		routeId("rota-agenda-create").
		convertBodyTo(String.class).
		to("validator:agenda.xsd").
			log("[${id}] Nova agenda.").
			multicast().
				to(this.toAutorLog).
				to(this.toCatalogoSave).
				to(this.toContatoUpdate);
	}
}
