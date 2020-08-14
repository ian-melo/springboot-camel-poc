package net.ism.sandbox.poc.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.springframework.stereotype.Service;

@Service
public class ContatoUpdateSubservice extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:subrota-perfil-update").
		routeId("subrota-perfil-update").
		convertBodyTo(String.class).
		setProperty("restBaseUrl", simple("${properties:rest.api.base.url}")).
		multicast().
			to("direct:subrota-perfil-autor-update").
			to("direct:subrota-perfil-contato-update");


		from("direct:subrota-perfil-autor-update").
		routeId("subrota-perfil-autor-update").
		convertBodyTo(String.class).
			split().xpath("/agenda/autor").
			transform(body().regexReplaceAll("autor", "perfil")).
		to("direct:subrota-perfil-rest-put");
//		to("mock:contato-update");


		from("direct:subrota-perfil-contato-update").
		routeId("subrota-perfil-contato-update").
		convertBodyTo(String.class).
			split().xpath("/agenda/contatos/contato").
			transform(body().regexReplaceAll("contato", "perfil")).
		to("direct:subrota-perfil-rest-put");
//		to("mock:contato-update");


		from("direct:subrota-perfil-rest-put").
		routeId("subrota-perfil-rest-put").
		convertBodyTo(String.class).
			filter().xpath("/perfil/nome[text()!='']|/perfil/email[text()!='']|/perfil/telefone[text()!='']").
			log("[${id}] Perfil XML:\n${body}").
			unmarshal().jacksonxml().marshal().json().
			log("[${id}] Perfil JSON:\n${body}").

			setHeader(Exchange.HTTP_METHOD, HttpMethods.PUT).
			setHeader(Exchange.HTTP_URI, simple("${exchangeProperty.restBaseUrl}/contato")).
			log("[${id}] Enviando PUT: ${header."+Exchange.HTTP_URI+"}").
		to("http://rest-api").
			convertBodyTo(String.class).
			log("[${id}] Resposta PUT: ${body} - (STATUS=${header."+Exchange.HTTP_RESPONSE_CODE+"})");
	}
}
