package es.weso.test;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import es.weso.business.CountryManagement;
import es.weso.model.Rank;

public class RankingTest {

	private static CountryManagement cm;

	@BeforeClass
	public static void setup() {
		cm = new CountryManagement();
	}

	@Test
	public void testIsoAlpha2SingleRank() {
		Rank es = cm.getRank("global", "2011", "es");
		Assert.assertEquals(18, es.getPosition());
		Assert.assertEquals(72.04, es.getValue());
	}

	@Test
	public void testIsoAlpha3SingleRank() {
		Rank se = cm.getRank("global", "2011", "swe");
		Assert.assertEquals(1, se.getPosition());
		Assert.assertEquals(100.00, se.getValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCountry() {
		cm.getRank("global", "2011", "wrong");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidYear() {
		cm.getRank("global", "wrong", "es");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCategory() {
		cm.getRank("wrong", "2011", "es");
	}

}
