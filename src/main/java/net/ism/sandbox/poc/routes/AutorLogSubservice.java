package net.ism.sandbox.poc.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@Service
public class AutorLogSubservice extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:subrota-autor-log").
		routeId("subrota-autor-log").
		convertBodyTo(String.class).
			log("[${id}] Logando autor.").
			process((exchange) -> {
				final String autor = xpath("/agenda/autor/id/text()").evaluate(exchange, String.class);
				final String visibilidade = xpath("/agenda/visibilidade/text()").evaluate(exchange, String.class);
				exchange.setProperty("registro", String.format("[Autor=%s, Visibilidade=%s]", autor, visibilidade));
				exchange.setProperty("data", xpath("/agenda/dataCriacao/text()").evaluate(exchange, String.class));
			}).
			setBody(simple("INSERT INTO registro(data, registro) VALUES ('${exchangeProperty.data}', '${exchangeProperty.registro}')")).
		to("jdbc:mysql").
			convertBodyTo(String.class).
			log("[${id}] Resultado SQL: ${body}");
	}
}
