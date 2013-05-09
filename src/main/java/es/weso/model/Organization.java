package es.weso.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import es.weso.annotations.LinkedDataEntity;
import es.weso.annotations.LinkedDataUri;

@JsonAutoDetect
@XmlRootElement
@LinkedDataEntity(type = "org:Organization")
/**
 * Representation of an organization
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 09/05/2013
 */
public class Organization {

	private String uri, webpage, name;

	@LinkedDataUri
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getWebpage() {
		return webpage;
	}

	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
