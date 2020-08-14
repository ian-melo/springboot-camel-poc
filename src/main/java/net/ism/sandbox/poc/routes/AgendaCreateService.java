package net.ism.sandbox.poc.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@Service
public class AgendaCreateService extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		errorHandler(
				deadLetterChannel("activemq:queue:poc.agenda_dead_letter").
				useOriginalMessage().
				logExhaustedMessageHistory(true).
				maximumRedeliveries(3).
				redeliveryDelay(3000).
				onRedelivery((exchange) -> {
					int it = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER);
					int n = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER);
					log.warn("[{}] Enviando para erro (tentativa {} de {})",
							simple("${id}").evaluate(exchange, String.class), it, n);
				}));

		from("activemq:queue:poc.agenda_create").
		routeId("rota-agenda-create").
		convertBodyTo(String.class).
			to("validator:agenda.xsd").
			log("[${id}] Nova agenda.").
			multicast().
				to("direct:subrota-autor-log").
				to("direct:subrota-catalogo-save").
				to("direct:subrota-perfil-update");
	}
}
