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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((indicator == null) ? 0 : indicator.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Observation other = (Observation) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (indicator == null) {
			if (other.indicator != null)
				return false;
		} else if (!indicator.equals(other.indicator))
			return false;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}
}
