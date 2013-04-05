package es.weso.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
/**
 * Representation of an observation
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 04/04/2013
 */
public class Observation {
	private Label country, indicator;
	private String year;
	private double value;

	public Observation(Label country, Label indicator, String year, double value) {
		this.country = country;
		this.indicator = indicator;
		this.year = year;
		this.value = value;
	}

	public Label getCountry() {
		return country;
	}

	public void setCountry(Label country) {
		this.country = country;
	}

	public Label getIndicator() {
		return indicator;
	}

	public void setIndicator(Label indicator) {
		this.indicator = indicator;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
