package net.ism.sandbox.poc.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CatalogoSaveSubservice extends RouteBuilder {
	private String soapBaseUrl;
	private String fromSave;
	private String toSaveConvert;
	private String toSaveSoap;
	private String toHttpSoap;

	@Autowired
	public CatalogoSaveSubservice(
			@Value("${soap.api.base.url}") String soapBaseUrl,
			@Value("${route.CatalogoSaveSubservice}") String fromSave,
			@Value("direct:subrota-catalogo-save-convert") String toSaveConvert,
			@Value("direct:subrota-catalogo-save-soap") String toSaveSoap,
			@Value("http://soap-api") String toHttpSoap) {
		super();
		this.soapBaseUrl = soapBaseUrl;
		this.fromSave = fromSave;
		this.toSaveConvert = toSaveConvert;
		this.toSaveSoap = toSaveSoap;
		this.toHttpSoap = toHttpSoap;
	}

	@Override
	public void configure() throws Exception {
		from(this.fromSave).
		routeId("subrota-catalogo-save").
		convertBodyTo(String.class).
			filter().xpath("/agenda/visibilidade[text()='PUBLICO']").
			log("[${id}] Agenda XML:\n${body}").
		to(this.toSaveConvert);


		from("direct:subrota-catalogo-save-convert").
		routeId("subrota-catalogo-save-convert").
		convertBodyTo(String.class).
			to("xslt:agenda-to-catalogo-soap.xslt").
			log("[${id}] Catalogo XML:\n${body}").
		to(this.toSaveSoap);


		from("direct:subrota-catalogo-save-soap").
		routeId("subrota-catalogo-save-soap").
		convertBodyTo(String.class).
			setHeader(Exchange.CONTENT_TYPE, constant("text/xml")).
			setHeader(Exchange.HTTP_URI, constant(this.soapBaseUrl+"/catalogo")).
			log("[${id}] Enviando SOAP/POST: ${header."+Exchange.HTTP_URI+"}").
		to(this.toHttpSoap).
			convertBodyTo(String.class).
			log("[${id}] Resposta SOAP/POST: ${body} - (STATUS=${header."+Exchange.HTTP_RESPONSE_CODE+"})");
	}
}
