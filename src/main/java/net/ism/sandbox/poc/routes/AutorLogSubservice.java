package net.ism.sandbox.poc.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AutorLogSubservice extends RouteBuilder {
	private String fromLog;
	private String toMysql;

	@Autowired
	public AutorLogSubservice(
			@Value("${route.AutorLogSubservice}") String fromLog,
			@Value("jdbc:mysql") String toMysql) {
		super();
		this.fromLog = fromLog;
		this.toMysql = toMysql;
	}

	@Override
	public void configure() throws Exception {
		from(this.fromLog).
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
		to(this.toMysql).
			convertBodyTo(String.class).
			log("[${id}] Resultado SQL: ${body}");
	}
}
