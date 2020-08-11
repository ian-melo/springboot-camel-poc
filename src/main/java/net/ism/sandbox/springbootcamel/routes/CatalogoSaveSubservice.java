package net.ism.sandbox.springbootcamel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@Service
public class CatalogoSaveSubservice extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:subrota-catalogo-save").
		routeId("subrota-catalogo-save").
		convertBodyTo(String.class).
			filter().xpath("/agenda/visibilidade[text()='PUBLICO']").
			setProperty("soapBaseUrl", simple("${properties:soap.api.base.url}")).
			log("[${id}] Agenda XML:\n${body}").
		to("xslt:agenda-to-catalogo-soap.xslt").
			log("[${id}] Catalogo XML:\n${body}").
			setHeader(Exchange.CONTENT_TYPE, constant("text/xml")).
			setHeader(Exchange.HTTP_URI, simple("${exchangeProperty.soapBaseUrl}/soap-service/catalogo")).
			log("[${id}] Enviando SOAP/POST: ${header."+Exchange.HTTP_URI+"}").
		to("http4://soap-api").
			convertBodyTo(String.class).
			log("[${id}] Resposta SOAP/POST: ${body} - (STATUS=${header."+Exchange.HTTP_RESPONSE_CODE+"})");
//		to("mock:catalogo-save");
	}
}
