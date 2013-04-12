package es.weso.test;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import es.weso.business.CountryManagement;
import es.weso.model.Observation;

public class ObservationTest {

	private static CountryManagement cm;

	@BeforeClass
	public static void setup() {
		cm = new CountryManagement();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCountry() {
		cm.getObservation("2011", "ITUD", "wrong");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidIndicator() {
		cm.getObservation("2011", "wrong", "es");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidYear() {
		cm.getObservation("wrong", "ITUD", "es");
	}

	@Test
	public void testIsoAlpha3SingleIndicator() {
		Observation seQ9ab2011 = cm.getObservation("2011", "Q9ab", "SWE");
		Assert.assertEquals(9.0, seQ9ab2011.getValue());
		Assert.assertEquals("2011", seQ9ab2011.getYear());
		Assert.assertEquals("SE", seQ9ab2011.getCountryName());
		Assert.assertEquals(
				"http://data.webfoundation.org/webindex/area/country/SWE",
				seQ9ab2011.getCountryUri());
		Assert.assertEquals("Q9ab", seQ9ab2011.getIndicatorName());
		Assert.assertEquals(
				"http://data.webfoundation.org/webindex/indicator/Q9ab",
				seQ9ab2011.getIndicatorUri());
	}

	@Test
	public void testIsoAlpha2SingleIndicator() {
		Observation esItud2011 = cm.getObservation("2011", "ITUD", "ES");
		Assert.assertEquals(114.233016778964, esItud2011.getValue());
		Assert.assertEquals("2011", esItud2011.getYear());
		Assert.assertEquals("ES", esItud2011.getCountryName());
		Assert.assertEquals(
				"http://data.webfoundation.org/webindex/area/country/ESP",
				esItud2011.getCountryUri());
		Assert.assertEquals("ITUD", esItud2011.getIndicatorName());
		Assert.assertEquals(
				"http://data.webfoundation.org/webindex/indicator/ITUD",
				esItud2011.getIndicatorUri());
	}
}