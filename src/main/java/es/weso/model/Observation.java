package es.weso.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import es.weso.annotations.LinkedDataUri;

@JsonAutoDetect
@XmlRootElement
/**
 * Representation of an observation
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 04/04/2013
 */
public class Observation {

	private String uri;
	private String label;
	private String areaName, areaUri, indicatorName, indicatorUri, year;
	private double value;

	@LinkedDataUri
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaUri() {
		return areaUri;
	}

	public void setAreaUri(String areaUri) {
		this.areaUri = areaUri;
	}

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public String getIndicatorUri() {
		return indicatorUri;
	}

	public void setIndicatorUri(String indicatorUri) {
		this.indicatorUri = indicatorUri;
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
		result = prime * result
				+ ((areaName == null) ? 0 : areaName.hashCode());
		result = prime * result + ((areaUri == null) ? 0 : areaUri.hashCode());
		result = prime * result
				+ ((indicatorName == null) ? 0 : indicatorName.hashCode());
		result = prime * result
				+ ((indicatorUri == null) ? 0 : indicatorUri.hashCode());
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
		if (areaName == null) {
			if (other.areaName != null)
				return false;
		} else if (!areaName.equals(other.areaName))
			return false;
		if (areaUri == null) {
			if (other.areaUri != null)
				return false;
		} else if (!areaUri.equals(other.areaUri))
			return false;
		if (indicatorName == null) {
			if (other.indicatorName != null)
				return false;
		} else if (!indicatorName.equals(other.indicatorName))
			return false;
		if (indicatorUri == null) {
			if (other.indicatorUri != null)
				return false;
		} else if (!indicatorUri.equals(other.indicatorUri))
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
