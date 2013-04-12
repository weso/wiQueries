package es.weso.model;

import java.util.Collection;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
@XmlRootElement
/**
 * Representation of a country
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 27/03/2013
 */
public class Country implements LinkedDataResource {

	private Label label, region;
	private String code_iso_alpha2, code_iso_alpha3;
	private int year;
	private double lat, lon;
	private Collection<Observation> observations;
	private Map<String, Rank> ranks;

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public Label getRegion() {
		return region;
	}

	public void setRegion(Label region) {
		this.region = region;
	}

	public String getCode_iso_alpha2() {
		return code_iso_alpha2;
	}

	public void setCode_iso_alpha2(String code_iso_alpha2) {
		this.code_iso_alpha2 = code_iso_alpha2;
	}

	public String getCode_iso_alpha3() {
		return code_iso_alpha3;
	}

	public void setCode_iso_alpha3(String code_iso_alpha3) {
		this.code_iso_alpha3 = code_iso_alpha3;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public Map<String, Rank> getRanks() {
		return ranks;
	}

	public void setRanks(Map<String, Rank> ranks) {
		this.ranks = ranks;
	}

	public Collection<Observation> getObservations() {
		return observations;
	}

	public void setObservations(Collection<Observation> observations) {
		this.observations = observations;
	}

	public String asRDF() {
		StringBuilder sb = new StringBuilder("<?xml version=\"1.0\"?>");
		sb.append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">");
		// TODO Auto-generated method stub
		return sb.toString();
	}

	public String asTTL() {
		// TODO Auto-generated method stub
		return null;
	}

}
