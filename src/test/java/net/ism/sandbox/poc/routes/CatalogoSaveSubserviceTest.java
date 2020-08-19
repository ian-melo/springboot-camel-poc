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
public class CatalogoSaveSubserviceTest extends AbstractJUnit4SpringContextTests {
	/* Valores para teste */
	private static final String BASE_URL = "http://localhost:4444";
	private static final String FROM_SAVE = "direct:fromSave";
	private static final String TO_SAVE_CONVERT = "mock:toSaveConvert";
	private static final String TO_SAVE_SOAP = "mock:toSaveSoap";
	private static final String TO_HTTP_SOAP = "mock:toHttpSoap";
	/* Valores iguais na rota */
	private static final String FROM_SAVE_CONVERT = "direct:subrota-catalogo-save-convert";
	private static final String FROM_SAVE_SOAP = "direct:subrota-catalogo-save-soap";
	/* Configuração */
    @Configuration
    public static class ContextConfig extends SingleRouteCamelConfiguration {
        @Bean
        public RouteBuilder route() {
        	return new CatalogoSaveSubservice(
    				BASE_URL,
    				FROM_SAVE,
    				TO_SAVE_CONVERT,
    				TO_SAVE_SOAP,
    				TO_HTTP_SOAP);
        }
    }
    @Produce(FROM_SAVE)
    protected ProducerTemplate fromSaveTemplate;
    @EndpointInject(TO_SAVE_CONVERT)
    protected MockEndpoint toSaveConvertEndpoint;
    @EndpointInject(TO_SAVE_SOAP)
    protected MockEndpoint toSaveSoapEndpoint;
    @EndpointInject(TO_HTTP_SOAP)
    protected MockEndpoint toHttpSoapEndpoint;
    @Produce(FROM_SAVE_CONVERT)
    protected ProducerTemplate fromSaveConvertTemplate;
    @Produce(FROM_SAVE_SOAP)
    protected ProducerTemplate fromSaveSoapTemplate;

    /* Testes */
    @DirtiesContext
    @Test
    public void when_fromSave_given_publico_then_pass() throws Exception {
    	toSaveConvertEndpoint.expectedBodiesReceived(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
    	fromSaveTemplate.sendBody(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
    	toSaveConvertEndpoint.assertIsSatisfied();
    }

    @DirtiesContext
    @Test
    public void when_fromSave_given_privado_then_filter() throws Exception {
    	toSaveConvertEndpoint.expectedMessageCount(0);
    	fromSaveTemplate.sendBody(SampleConstants.VALID_ALLINFO_PRIVADO_AGENDA_XML());
    	toSaveConvertEndpoint.assertIsSatisfied();
    }

    @DirtiesContext
    @Test
    public void when_fromSaveConvert_given_allInfo_then_convert() throws Exception {
    	toSaveSoapEndpoint.expectedBodiesReceived(SampleConstants.VALID_ALLINFO_CATALOGO_XML());
    	fromSaveConvertTemplate.sendBody(SampleConstants.VALID_ALLINFO_PUBLICO_AGENDA_XML());
    	toSaveSoapEndpoint.assertIsSatisfied();
    }

    @DirtiesContext
    @Test
    public void when_fromSaveConvert_given_someInfo_then_convert() throws Exception {
    	toSaveSoapEndpoint.expectedBodiesReceived(SampleConstants.VALID_SOMEINFO_CATALOGO_XML());
    	fromSaveConvertTemplate.sendBody(SampleConstants.VALID_SOMEINFO_PUBLICO_AGENDA_XML());
    	toSaveSoapEndpoint.assertIsSatisfied();
    }

    @DirtiesContext
    @Test
    public void when_fromSaveConvert_given_onlyInfo_then_convert() throws Exception {
    	toSaveSoapEndpoint.expectedBodiesReceived(SampleConstants.VALID_ONLYINFO_CATALOGO_XML());
    	fromSaveConvertTemplate.sendBody(SampleConstants.VALID_ONLYINFO_PUBLICO_AGENDA_XML());
    	toSaveSoapEndpoint.assertIsSatisfied();
    }

}
