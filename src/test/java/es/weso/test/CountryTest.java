package es.weso.test;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import es.weso.business.CountryManagement;
import es.weso.model.Country;

public class CountryTest {

	private static CountryManagement cm;

	@BeforeClass
	public static void setup() {
		cm = new CountryManagement();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidYear() {
		cm.getCountry("wrong", "es");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCountry() {
		cm.getCountry("2011", "wrong");
	}

	@Test
	public void testIsoAlpha3() {
		Country country = cm.getCountry("2011", "ESP");
		Assert.assertEquals("Spain", country.getName());
		Assert.assertTrue(country.getObservations().contains(
				cm.getObservation("2011", "ITUD", "ES")));
		Assert.assertEquals(18, country.getRanks().get("global").getPosition());
	}

	@Test
	public void testIsoAlpha2() {
		Country country = cm.getCountry("2011", "SE");
		Assert.assertEquals("Sweden", country.getName());
		Assert.assertTrue(country.getObservations().contains(
				cm.getObservation("2011", "ITUD", "SWE")));
		Assert.assertEquals(1, country.getRanks().get("global").getPosition());
	}
}
