package es.weso.data;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import es.weso.model.Country;
import es.weso.model.Label;
import es.weso.model.Observation;
import es.weso.model.Rank;
import es.weso.model.RankMap;
import es.weso.util.Conf;
import es.weso.util.JenaMemcachedClient;

/**
 * Class to get data of a specific {@link Country}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 27/03/2013
 */
public class CountryData {

	private static Logger log = Logger.getLogger(CountryData.class);
	private JenaMemcachedClient client;

	public CountryData() {
		client = new JenaMemcachedClient();
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
		checkValidYear(year);
		ResultSet rs = client.executeQuery(getQueryString(category, year));
		return new RankMap(rs).getData().get(countrycode);
	}

	/**
	 * Gets the {@link Rank}s of a {@link Country} observed in a specific year
	 * 
	 * @param year
	 *            The year to be checked
	 * @param countryCode
	 *            The iso-alpha-2 code of the {@link Country} to be checked
	 * @return The {@link Rank}s of a {@link Country} observed in a specific
	 *         year
	 */
	public Map<String, Rank> getRank(String year, String countryCode) {
		checkValidYear(year);
		String[] availableCategories = Conf.getConfig("available.categories")
				.split(";");
		Map<String, Rank> ranks = new HashMap<String, Rank>();
		for (String category : availableCategories) {
			ResultSet rs = client.executeQuery(getQueryString(category, year));
			ranks.put(category, new RankMap(rs).getData().get(countryCode));
		}
		return ranks;
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
		checkValidYear(year);
		checkValidCountryCode(countryCode);
		Collection<Observation> observations = new ArrayDeque<Observation>(
				getAvailableIndicators().size());
		ResultSet rs = client.executeQuery(Conf.getQuery("all.observations",
				year, countryCode));
		while (rs.hasNext()) {
			try {
				observations.add(querySolutionToObservation(rs.next()));
			} catch (URISyntaxException e) {
				log.info("Found an invalid URI ", e);
			}
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
		checkValidYear(year);
		checkValidCountryCode(countryCode);
		checkValidIndicator(indicator);
		ResultSet rs = client.executeQuery(getQueryString(year, indicator,
				countryCode));
		try {
			return querySolutionToObservation(rs.next());
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("Invalid indicator " + indicator
					+ " for year " + year + " and country " + countryCode, e);
		} catch (URISyntaxException e) {
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
		for (String year : years) {
			checkValidYear(year);
		}
		for (String countryCode : countryCodes) {
			checkValidCountryCode(countryCode);
		}
		Collection<Observation> observations = new ArrayDeque<Observation>(
				years.size() * indicators.size() * countryCodes.size());
		ResultSet rs = client.executeQuery(getQueryString(years, indicators,
				countryCodes));
		while (rs.hasNext()) {
			try {
				observations.add(querySolutionToObservation(rs.next()));
			} catch (URISyntaxException e) {
				log.info("Found an invalid URI ", e);
			}
		}
		return observations;
	}

	/**
	 * Gets a {@link Country} with {@link Observation}s and {@link Rank}s in a
	 * specific year
	 * 
	 * @param year
	 *            The year to be observed
	 * @param countryCode
	 *            The {@link Country} to be observed
	 * @return The {@link Country} with {@link Observation}s and {@link Rank}s
	 *         in a specific year
	 */
	public Country getCountry(String year, String countryCode) {
		checkValidYear(year);
		checkValidCountryCode(countryCode);
		Country country = getCountryData(countryCode);
		country.setObservations(getObservation(year, countryCode));
		country.setRanks(getRank(year, countryCode));
		country.setCode_iso_alpha2(countryCode);
		country.setYear(Integer.parseInt(year));
		return country;
	}

	/**
	 * Gets country specific data
	 * 
	 * @param countryCode
	 *            The code of the {@link Country} to be retrieved
	 * @return A {@link Country} without observations and ranks
	 */
	private Country getCountryData(String countryCode) {
		Country country = new Country();
		ResultSet rs = client.executeQuery(Conf.getQuery("country.data",
				countryCode));
		try {
			QuerySolution qs = rs.next();
			country.setCode_iso_alpha3(qs.getLiteral("id").getString());
			try {
				Label label = new Label();
				label.setUri(new URI(qs.getResource("country").getURI()));
				label.setValue(qs.getLiteral("name").getString());
				country.setLabel(label);
			} catch (URISyntaxException e) {
				log.error("Found invalid URI", e);
			}
			country.setLat(qs.getLiteral("lat").getDouble());
			country.setLon(qs.getLiteral("lon").getDouble());
			try {
				Label region = new Label();
				region.setUri(new URI(qs.getResource("region").getURI()));
				region.setValue(qs.getLiteral("regionName").getString());
				country.setRegion(region);
			} catch (URISyntaxException e) {
				log.error("Found invalid URI", e);
			}
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
	private Collection<String> getAvailableIndicators() {
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
	 * @throws URISyntaxException
	 *             if an {@link URI} of the result is malformed
	 */
	private Observation querySolutionToObservation(QuerySolution qs)
			throws URISyntaxException {
		Label country = new Label();
		country.setUri(new URI(qs.getResource("country").getURI()));
		country.setValue(qs.getLiteral("code").getString());
		Label indicator = new Label();
		indicator.setUri(new URI(qs.getResource("indicator").getURI()));
		indicator.setValue(qs.getLiteral("indicatorLabel").getString());
		return new Observation(country, indicator, qs.getLiteral("year")
				.getString(), qs.getLiteral("value").getDouble());
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

	/**
	 * Checks if a string is a valid iso-alpha2 country code
	 * 
	 * @param countryCode
	 *            The string to be checked
	 */
	private void checkValidCountryCode(String countryCode) {
		if (!countryCode.matches("[A-Z]{2}")) {
			throw new IllegalArgumentException(countryCode
					+ " is not a valid countryCode");
		}
	}

	/**
	 * Checks if a string is a valid year
	 * 
	 * @param year
	 *            The year to be checked
	 */
	private void checkValidYear(String year) {
		if (!year.matches("\\d+")) {
			throw new IllegalArgumentException(year + " is not a valid year");
		}
	}

	/**
	 * Checks if a string is a valid indicator
	 * 
	 * @param indicator
	 *            The indicator to be checked
	 */
	private void checkValidIndicator(String indicator) {
		if (!getAvailableIndicators().contains(indicator)) {
			throw new IllegalArgumentException(indicator
					+ " is not a valid indicator");
		}
	}

}
