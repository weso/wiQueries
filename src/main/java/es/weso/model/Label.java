package es.weso.model;

import java.net.URI;

public class Label {
	private String value;
	private URI uri;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}
}
