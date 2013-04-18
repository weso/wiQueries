package es.weso.data;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import es.weso.model.Country;
import es.weso.model.Observation;
import es.weso.model.Rank;
import es.weso.model.RankMap;
import es.weso.util.Conf;
import es.weso.util.JenaMemcachedClient;

/**
 * Class to get data of a specific {@link Country} from the
 * {@link JenaMemcachedClient}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 27/03/2013
 */
public class CountryData {

	private JenaMemcachedClient client;

	public CountryData() throws IOException {
		client = JenaMemcachedClient.create();
	}

	/**
	 * Gets the {@link Rank} of a {@link Country} in a specific category
	 * observed in a specific year
	 * 
	 * @param category
	 *            The category of the rank
	 * @param year
	 *            The year to be checked
	 * @param countrycode
	 *            The iso-alpha-2 code of the {@link Country} to be checked
	 * @return The {@link Rank} of a {@link Country} in a specific category
	 *         observed in a specific year
	 */
	public Rank getRank(String category, String year, String countrycode) {
		ResultSet rs = client.executeQuery(getQueryString(category, year));
		return new RankMap(rs).getData().get(countrycode);
	}

	/**
	 * Converts a iso-alpha-3 code into a iso-alpha-2 code
	 * 
	 * @param countryCode
	 *            The iso-alpha-3 code to be converted
	 * @return The converted iso-alpha-2 code
	 */
	public String convertCountryCode(String countryCode) {
		ResultSet rs = client.executeQuery(Conf.getQuery("convert.code",
				countryCode));
		try {
			return rs.next().getLiteral("code").getString();
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException(countryCode
					+ " is not a valid countryCode");
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(countryCode
					+ " is not a valid countryCode");
		}
	}

	/**
	 * Gets the {@link Observation}s of a {@link Country} in a specific year
	 * 
	 * @param year
	 *            The year to be checked
	 * @param countryCode
	 *            The iso-alpha-2 code of the {@link Country} to be checked
	 * @return The {@link Observation}s of a {@link Country} in a specific year
	 */
	public Collection<Observation> getObservation(String year,
			String countryCode) {
		Collection<Observation> observations = new ArrayDeque<Observation>(
				getAvailableIndicators().size());
		ResultSet rs = client.executeQuery(Conf.getQuery("all.observations",
				year, countryCode));
		while (rs.hasNext()) {
			observations.add(querySolutionToObservation(rs.next()));
		}
		return observations;
	}

	/**
	 * Gets the {@link Observation} of a {@link Country} produced by a specific
	 * indicator in a specific year
	 * 
	 * @param year
	 *            The year to be checked
	 * @param indicator
	 *            The indicator to be checked
	 * @param countryCode
	 *            The iso-alpha-2 code of the {@link Country} to be checked
	 * @return The {@link Observation} of a {@link Country} produced by a
	 *         specific indicator in a specific year
	 */
	public Observation getObservation(String year, String indicator,
			String countryCode) {
		ResultSet rs = client.executeQuery(getQueryString(year, indicator,
				countryCode));
		try {
			return querySolutionToObservation(rs.next());
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("Invalid indicator " + indicator
					+ " for year " + year + " and country " + countryCode, e);
		}
	}

	/**
	 * Gets the {@link Observation}s of several {@link Country countries}
	 * produced by a some indicators in some specific years
	 * 
	 * @param years
	 *            The years to be checked
	 * @param indicator
	 *            The indicators to be checked
	 * @param countryCodes
	 *            The iso-alpha-2 codes of the {@link Country countries} to be
	 *            checked
	 * @return The {@link Observation}s of several {@link Country countries}
	 *         produced by a some indicators in some specific years
	 */
	public Collection<Observation> getObservations(Collection<String> years,
			Collection<String> indicators, Collection<String> countryCodes) {
		Collection<Observation> observations = new ArrayDeque<Observation>(
				years.size() * indicators.size() * countryCodes.size());
		ResultSet rs = client.executeQuery(getQueryString(years, indicators,
				countryCodes));
		while (rs.hasNext()) {
			observations.add(querySolutionToObservation(rs.next()));
		}
		return observations;
	}

	/**
	 * Gets country specific data
	 * 
	 * @param countryCode
	 *            The code of the {@link Country} to be retrieved
	 * @return A {@link Country} without observations and ranks
	 */
	public Country getCountryData(String countryCode) {
		Country country = new Country();
		ResultSet rs = client.executeQuery(Conf.getQuery("country.data",
				countryCode));
		try {
			QuerySolution qs = rs.next();
			country.setCode_iso_alpha3(qs.getLiteral("id").getString());
			country.setName(qs.getLiteral("name").getString());
			country.setUri(qs.getResource("country").getURI());
			country.setLat(qs.getLiteral("lat").getDouble());
			country.setLon(qs.getLiteral("lon").getDouble());
			country.setRegionName(qs.getLiteral("regionName").getString());
			country.setRegionUri(qs.getResource("region").getURI());
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("Invalid countryCode", e);
		}
		return country;
	}

	/**
	 * Gets the name of all the available indicators
	 * 
	 * @return The name of all the available indicators
	 */
	public Collection<String> getAvailableIndicators() {
		Collection<String> indicators = new HashSet<String>();
		ResultSet rs = client.executeQuery(Conf.getQuery("indicators"));
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			indicators.add(qs.getLiteral("name").getString());
		}
		return indicators;
	}

	/**
	 * Converts a {@link QuerySolution} into an {@link Observation}
	 * 
	 * @param qs
	 *            The {@link QuerySolution} to be converted
	 * @return The resulting {@link Observation}
	 */
	private Observation querySolutionToObservation(QuerySolution qs) {
		Observation obs = new Observation();
		obs.setCountryName(qs.getLiteral("code").getString());
		obs.setCountryUri(qs.getResource("country").getURI());
		obs.setIndicatorName(qs.getLiteral("indicatorLabel").getString());
		obs.setIndicatorUri(qs.getResource("indicator").getURI());
		obs.setValue(qs.getLiteral("value").getDouble());
		obs.setYear(qs.getLiteral("year").getString());
		obs.setUri(qs.getResource("obs").getURI());
		obs.setLabel(qs.getLiteral("label").getString());
		return obs;
	}

	/**
	 * Gets the query string for a specific year, indicator, and countryCode
	 * 
	 * @param year
	 *            The year to be filtered
	 * @param indicator
	 *            The indicator to be filtered
	 * @param countryCode
	 *            The countryCode to be filtered
	 * @return The query string for a specified year, indicator, and countryCode
	 */
	private String getQueryString(String year, String indicator,
			String countryCode) {
		String query;
		try {
			query = Conf.getQuery("indicator.year.country", indicator, year,
					countryCode);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Unknown query", e);
		}
		return query;
	}

	/**
	 * Gets the query string for some specific years, indicators, and
	 * countryCodes
	 * 
	 * @param years
	 *            The years to be filtered
	 * @param indicators
	 *            The indicators to be filtered
	 * @param countryCodes
	 *            The countryCodes to be filtered
	 * @return The query string for a specified year, indicator, and countryCode
	 */
	@SuppressWarnings("unchecked")
	private String getQueryString(Collection<String> years,
			Collection<String> indicators, Collection<String> countryCodes) {
		String query;
		try {
			query = Conf.getQueryWithFilters("indicator.year.country",
					indicators, years, countryCodes);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Unknown query", e);
		}
		return query;
	}

	/**
	 * Gets the query string for a specific category and year
	 * 
	 * @param category
	 *            The category of the query
	 * @param year
	 *            The year to be filtered
	 * @return The query string for the specified category and year
	 */
	private String getQueryString(String category, String year) {
		String query;
		try {
			query = Conf.getQuery(category, year);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(category
					+ " is an invalid category", e);
		}
		return query;
	}

}
