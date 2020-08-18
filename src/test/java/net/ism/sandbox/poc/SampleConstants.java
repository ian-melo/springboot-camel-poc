package net.ism.sandbox.poc;

import java.util.HashMap;
import java.util.Map;

public class SampleConstants {
	private enum InfoLevel {
		ALL, SOME, ONLY
	}

	private static String VALID_AGENDA_XML(String visibilidade, InfoLevel infoLevel) {
		StringBuilder sb = new StringBuilder().append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>").append("<agenda>");
		switch(infoLevel) {
		case ALL:
			sb.append(VALID_ALLINFO_AGENDA_AUTOR_XML()); break;
		case ONLY:
			sb.append(VALID_ONLYINFO_AGENDA_AUTOR_XML()); break;
		case SOME:
			sb.append(VALID_SOMEINFO_AGENDA_AUTOR_XML()); break;
		default:
			break;
		}
		sb.append("<dataCriacao>2020-08-06T13:26:48.233-03:00</dataCriacao>")
			.append("<visibilidade>"+visibilidade+"</visibilidade>")
			.append("<contatos>");
		switch(infoLevel) {
		case ALL:
			sb.append(VALID_ALLINFO_AGENDA_CONTATO_XML()); sb.append(VALID_ALLINFO_AGENDA_CONTATO_XML()); break;
		case ONLY:
			sb.append(VALID_ONLYINFO_AGENDA_CONTATO_XML()); sb.append(VALID_ONLYINFO_AGENDA_CONTATO_XML()); break;
		case SOME:
			sb.append(VALID_SOMEINFO_AGENDA_CONTATO_XML()); sb.append(VALID_SOMEINFO_AGENDA_CONTATO_XML()); break;
		default:
			break;
		}
		sb.append("</contatos>").append("</agenda>");
		return sb.toString();
	}
	public static String VALID_ALLINFO_PUBLICO_AGENDA_XML() { return VALID_AGENDA_XML("PUBLICO", InfoLevel.ALL); }
	public static String VALID_ONLYINFO_PUBLICO_AGENDA_XML() { return VALID_AGENDA_XML("PUBLICO", InfoLevel.ONLY); }
	public static String VALID_SOMEINFO_PUBLICO_AGENDA_XML() { return VALID_AGENDA_XML("PUBLICO", InfoLevel.SOME); }

	public static String VALID_ALLINFO_PRIVADO_AGENDA_XML() { return VALID_AGENDA_XML("PRIVADO", InfoLevel.ALL); }
	public static String VALID_ONLYINFO_PRIVADO_AGENDA_XML() { return VALID_AGENDA_XML("PRIVADO", InfoLevel.ONLY); }
	public static String VALID_SOMEINFO_PRIVADO_AGENDA_XML() { return VALID_AGENDA_XML("PRIVADO", InfoLevel.SOME); }

	public static String VALID_ALLINFO_RESTRITO_AGENDA_XML() { return VALID_AGENDA_XML("RESTRITO", InfoLevel.ALL); }
	public static String VALID_ONLYINFO_RESTRITO_AGENDA_XML() { return VALID_AGENDA_XML("RESTRITO", InfoLevel.ONLY); }
	public static String VALID_SOMEINFO_RESTRITO_AGENDA_XML() { return VALID_AGENDA_XML("RESTRITO", InfoLevel.SOME); }

	private static String VALID_AGENDA_PERFIL_OBJECT_XML(String element, InfoLevel infoLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append("<"+element+">").append("<id>1020</id>");
		switch(infoLevel) {
		case ALL:
			sb.append("<nome>Marcos Ribas</nome>");
			sb.append("<email>marcos.ribas@email.com</email>");
			sb.append("<telefone>+55 19 932909285</telefone>");
			break;
		case SOME:
			sb.append("<nome>Marcos Ribas</nome>");
			sb.append("<telefone>+55 19 932909285</telefone>");
			break;
		case ONLY:
		default:
			break;
		}
		sb.append("</"+element+">");
		return sb.toString();
	}
	public static String VALID_ALLINFO_AGENDA_AUTOR_XML() { return VALID_AGENDA_PERFIL_OBJECT_XML("autor", InfoLevel.ALL); }
	public static String VALID_ONLYINFO_AGENDA_AUTOR_XML() { return VALID_AGENDA_PERFIL_OBJECT_XML("autor", InfoLevel.ONLY); }
	public static String VALID_SOMEINFO_AGENDA_AUTOR_XML() { return VALID_AGENDA_PERFIL_OBJECT_XML("autor", InfoLevel.SOME); }

	public static String VALID_ALLINFO_AGENDA_CONTATO_XML() { return VALID_AGENDA_PERFIL_OBJECT_XML("contato", InfoLevel.ALL); }
	public static String VALID_ONLYINFO_AGENDA_CONTATO_XML() { return VALID_AGENDA_PERFIL_OBJECT_XML("contato", InfoLevel.ONLY); }
	public static String VALID_SOMEINFO_AGENDA_CONTATO_XML() { return VALID_AGENDA_PERFIL_OBJECT_XML("contato", InfoLevel.SOME); }

	public static String VALID_ALLINFO_AGENDA_PERFIL_XML() { return VALID_AGENDA_PERFIL_OBJECT_XML("perfil", InfoLevel.ALL); }
	public static String VALID_ONLYINFO_AGENDA_PERFIL_XML() { return VALID_AGENDA_PERFIL_OBJECT_XML("perfil", InfoLevel.ONLY); }
	public static String VALID_SOMEINFO_AGENDA_PERFIL_XML() { return VALID_AGENDA_PERFIL_OBJECT_XML("perfil", InfoLevel.SOME); }

	private static Map<String,Object> VALID_AGENDA_PERFIL_OBJECT_MAP(InfoLevel infoLevel) {
		Map<String, Object> map = new HashMap<>();
		map.put("id","1020");
		switch(infoLevel) {
		case ALL:
			map.put("email","marcos.ribas@email.com");
		case SOME:
			map.put("nome","Marcos Ribas");
			map.put("telefone","+55 19 932909285");
		case ONLY:
		default:
			break;
		}
		return map;
	}
	public static Map<String,Object> VALID_ALLINFO_AGENDA_PERFIL_MAP() { return VALID_AGENDA_PERFIL_OBJECT_MAP(InfoLevel.ALL); }
	public static Map<String,Object> VALID_ONLYINFO_AGENDA_PERFIL_MAP() { return VALID_AGENDA_PERFIL_OBJECT_MAP(InfoLevel.ONLY); }
	public static Map<String,Object> VALID_SOMEINFO_AGENDA_PERFIL_MAP() { return VALID_AGENDA_PERFIL_OBJECT_MAP(InfoLevel.SOME); }

	private static String VALID_CATALOGO_XML(InfoLevel infoLevel) {
		StringBuilder sb = new StringBuilder().append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
			.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:catalogo=\"http://registro.com.br/catalogo\">")
		    .append("<soapenv:Body>").append("<catalogo:catalogo>");
		if(InfoLevel.ONLY == infoLevel)
			sb.append("<catalogo:itens/>");
		else
			sb.append("<catalogo:itens>")
				.append(VALID_CATALOGO_ITEM_XML(infoLevel))
				.append(VALID_CATALOGO_ITEM_XML(infoLevel))
				.append(VALID_CATALOGO_ITEM_XML(infoLevel))
				.append("</catalogo:itens>");
		sb.append("</catalogo:catalogo>").append("</soapenv:Body>").append("</soapenv:Envelope>");
		return sb.toString();
	}
	public static String VALID_ALLINFO_CATALOGO_XML() { return VALID_CATALOGO_XML(InfoLevel.ALL); }
	public static String VALID_ONLYINFO_CATALOGO_XML() { return VALID_CATALOGO_XML(InfoLevel.ONLY); }
	public static String VALID_SOMEINFO_CATALOGO_XML() { return VALID_CATALOGO_XML(InfoLevel.SOME); }

	private static String VALID_CATALOGO_ITEM_XML(InfoLevel infoLevel) {
		StringBuilder sb = new StringBuilder().append("");
		switch(infoLevel) {
		case ALL:
			sb.append("<catalogo:item>")
	        	.append("<catalogo:email>marcos.ribas@email.com</catalogo:email>")
	        	.append("<catalogo:telefone>+55 19 932909285</catalogo:telefone>")
	        	.append("<catalogo:data>2020-08-06T13:26:48.233-03:00</catalogo:data>")
	        .append("</catalogo:item>");
			break;
		case SOME:
			sb.append("<catalogo:item>")
	        	.append("<catalogo:telefone>+55 19 932909285</catalogo:telefone>")
	        	.append("<catalogo:data>2020-08-06T13:26:48.233-03:00</catalogo:data>")
	        .append("</catalogo:item>");
			break;
		case ONLY:
		default:
			break;
		}
		return sb.toString();
	}

}
