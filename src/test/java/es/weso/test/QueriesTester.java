package es.weso.test;

import junit.framework.Assert;

import org.junit.Test;

import es.weso.data.CountryData;
import es.weso.model.Observation;
import es.weso.model.Rank;

public class QueriesTester {

	@Test
	public void testRank() {
		CountryData cd = new CountryData();

		Rank es = cd.getRank("global", "2011", "es");
		Assert.assertEquals(18, es.getPosition());
		Assert.assertEquals(72.04, es.getValue());

		Rank se = cd.getRank("global", "2011", "se");
		Assert.assertEquals(1, se.getPosition());
		Assert.assertEquals(100.00, se.getValue());

		Rank wrong = cd.getRank("global", "2011", "wrong");
		Assert.assertNull(wrong);
		try {
			wrong = cd.getRank("global", "wrong", "es");
			Assert.fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			Assert.fail();
		}
		try {
			wrong = cd.getRank("wrong", "2011", "es");
			Assert.fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testSingleIndicator() {
		CountryData cd = new CountryData();

		Observation esItud2011 = cd.getObservation("2011", "ITUD", "ES");
		Assert.assertEquals(114.233016778964, esItud2011.getValue());
		Assert.assertEquals("2011", esItud2011.getYear());
		Assert.assertEquals("ES", esItud2011.getCountry().getValue());
		Assert.assertEquals(
				"http://data.webfoundation.org/webindex/area/country/ESP",
				esItud2011.getCountry().getUri().toString());
		Assert.assertEquals("ITUD", esItud2011.getIndicator().getValue());
		Assert.assertEquals(
				"http://data.webfoundation.org/webindex/indicator/ITUD",
				esItud2011.getIndicator().getUri().toString());

		Observation seQ9ab2011 = cd.getObservation("2011", "Q9ab", "SE");
		Assert.assertEquals(9.0, seQ9ab2011.getValue());
		Assert.assertEquals("2011", seQ9ab2011.getYear());
		Assert.assertEquals("SE", seQ9ab2011.getCountry().getValue());
		Assert.assertEquals(
				"http://data.webfoundation.org/webindex/area/country/SWE",
				seQ9ab2011.getCountry().getUri().toString());
		Assert.assertEquals("Q9ab", seQ9ab2011.getIndicator().getValue());
		Assert.assertEquals(
				"http://data.webfoundation.org/webindex/indicator/Q9ab",
				seQ9ab2011.getIndicator().getUri().toString());

		try {
			cd.getObservation("2011", "wrong", "SE");
			Assert.fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			Assert.fail();
		}
		try {
			cd.getObservation("2011", "Q9ab", "wrong");
			Assert.fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			Assert.fail();
		}
		try {
			cd.getObservation("wrong", "Q9ab", "SE");
			Assert.fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			Assert.fail();
		}

	}
}
