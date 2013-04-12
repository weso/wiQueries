package es.weso.model;

import java.util.Collection;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import es.weso.annotations.LinkedDataEntity;
import es.weso.annotations.LinkedDataProperty;
import es.weso.annotations.LinkedDataUri;

@JsonAutoDetect
@XmlRootElement
@LinkedDataEntity(type = "http://data.webfoundation.org/webindex/ontology/Country")
/**
 * Representation of a country
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 27/03/2013
 */
public class Country {

	private String name, uri, regionName, regionUri, code_iso_alpha2,
			code_iso_alpha3;;
	private int year;
	private double lat, lon;
	private Collection<Observation> observations;
	private Map<String, Rank> ranks;

	@LinkedDataProperty(predicate = "http://www.w3.org/2000/01/rdf-schema#label")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@LinkedDataUri
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@LinkedDataProperty(predicate = "http://www.w3.org/2000/01/rdf-schema#label", inverse = true)
	public String getRegionUri() {
		return regionUri;
	}

	public void setRegionUri(String regionUri) {
		this.regionUri = regionUri;
	}

	@LinkedDataProperty(predicate = "http://data.webfoundation.org/webindex/ontology/has-iso-alpha2-code")
	public String getCode_iso_alpha2() {
		return code_iso_alpha2;
	}

	public void setCode_iso_alpha2(String code_iso_alpha2) {
		this.code_iso_alpha2 = code_iso_alpha2;
	}

	@LinkedDataProperty(predicate = "http://data.webfoundation.org/webindex/ontology/has-iso-alpha3-code")
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

	@LinkedDataProperty(predicate = "http://www.w3.org/2003/01/geo/wgs84_pos#lat")
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	@LinkedDataProperty(predicate = "http://www.w3.org/2003/01/geo/wgs84_pos#long")
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

	@LinkedDataProperty(predicate = "http://data.webfoundation.org/webindex/ontology/ref-area", inverse = true)
	public Collection<Observation> getObservations() {
		return observations;
	}

	public void setObservations(Collection<Observation> observations) {
		this.observations = observations;
	}

}
