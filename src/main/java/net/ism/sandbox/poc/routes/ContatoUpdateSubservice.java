package net.ism.sandbox.poc.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContatoUpdateSubservice extends RouteBuilder {
	private String restBaseUrl;
	private String fromUpdate;
	private String toUpdateAutor;
	private String toUpdateContato;
	private String toUpdateConvert;
	private String toUpdateRest;
	private String toHttpRest;

	@Autowired
	public ContatoUpdateSubservice(
			@Value("${rest.api.base.url}") String restBaseUrl,
			@Value("${route.ContatoUpdateSubservice}") String fromUpdate,
			@Value("direct:subrota-perfil-update-autor") String toUpdateAutor,
			@Value("direct:subrota-perfil-update-contato") String toUpdateContato,
			@Value("direct:subrota-perfil-update-convert") String toUpdateConvert,
			@Value("direct:subrota-perfil-update-rest") String toUpdateRest,
			@Value("http://rest-api") String toHttpRest) {
		super();
		this.restBaseUrl = restBaseUrl;
		this.fromUpdate = fromUpdate;
		this.toUpdateAutor = toUpdateAutor;
		this.toUpdateContato = toUpdateContato;
		this.toUpdateConvert = toUpdateConvert;
		this.toUpdateRest = toUpdateRest;
		this.toHttpRest = toHttpRest;
	}

	@Override
	public void configure() throws Exception {
		from(this.fromUpdate).
		routeId("subrota-perfil-update").
		convertBodyTo(String.class).
		multicast().
			to(this.toUpdateAutor).
			to(this.toUpdateContato);


		from("direct:subrota-perfil-update-autor").
		routeId("subrota-perfil-update-autor").
		convertBodyTo(String.class).
			split().xpath("/agenda/autor").
			transform(body().regexReplaceAll("autor", "perfil")).
		to(this.toUpdateConvert);


		from("direct:subrota-perfil-update-contato").
		routeId("subrota-perfil-update-contato").
		convertBodyTo(String.class).
			split().xpath("/agenda/contatos/contato").
			transform(body().regexReplaceAll("contato", "perfil")).
		to(this.toUpdateConvert);


		from("direct:subrota-perfil-update-convert").
		routeId("subrota-perfil-update-convert").
		convertBodyTo(String.class).
			filter().xpath("/perfil/nome[text()!='']|/perfil/email[text()!='']|/perfil/telefone[text()!='']").
			log("[${id}] Perfil XML:\n${body}").
			unmarshal().jacksonxml().marshal().json().
			log("[${id}] Perfil JSON:\n${body}").
		to(this.toUpdateRest);


		from("direct:subrota-perfil-update-rest").
		routeId("subrota-perfil-update-rest").
		convertBodyTo(String.class).
			setHeader(Exchange.HTTP_METHOD, HttpMethods.PUT).
			setHeader(Exchange.HTTP_URI, constant(this.restBaseUrl+"/contato")).
			log("[${id}] Enviando PUT: ${header."+Exchange.HTTP_URI+"}").
		to(this.toHttpRest).
			convertBodyTo(String.class).
			log("[${id}] Resposta PUT: ${body} - (STATUS=${header."+Exchange.HTTP_RESPONSE_CODE+"})");
	}
}
