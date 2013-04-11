package es.weso.business;

import java.util.ArrayDeque;
import java.util.Collection;

import es.weso.data.CountryData;
import es.weso.model.Country;
import es.weso.model.Observation;
import es.weso.model.ObservationCollection;
import es.weso.model.Rank;
import es.weso.model.RankMap;
import es.weso.util.Conf;

/**
 * Class to get data of a specific {@link Country}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 10/04/2013
 */
public class CountryManagement {

	private CountryData data;

	public CountryManagement() {
		data = new CountryData();
	}

	/**
	 * Gets the {@link Rank} of a {@link Country} in a specific category
	 * observed in a specific year
	 * 
	 * @param category
	 *            The category of the rank
	 * @param year
	 *            The year to be checked
	 * @param countryCode
	 *            The iso-alpha-2 or iso-alpha-3 code of the {@link Country} to
	 *            be checked
	 * @return The {@link Rank} of a {@link Country} in a specific category
	 *         observed in a specific year
	 */
	public Rank getRank(String category, String year, String countryCode) {
		checkValidYear(year);
		countryCode = checkValidCountryCode(countryCode);
		return data.getRank(category, year, countryCode);
	}

	/**
	 * Gets the {@link Rank}s of a {@link Country} observed in a specific year
	 * 
	 * @param year
	 *            The year to be checked
	 * @param countryCode
	 *            The iso-alpha-2 or iso-alpha-3 code of the {@link Country} to
	 *            be checked
	 * @return The {@link Rank}s of a {@link Country} observed in a specific
	 *         year
	 */
	public RankMap getRank(String year, String countryCode) {
		RankMap ranks = new RankMap();
		String[] availableCategories = Conf.getConfig("available.categories")
				.split(";");
		for (String category : availableCategories) {
			ranks.getData().put(category, getRank(category, year, countryCode));
		}
		return ranks;
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
	 *            The iso-alpha-2 or iso-alpha-3 code of the {@link Country} to
	 *            be checked
	 * @return The {@link Observation} of a {@link Country} produced by a
	 *         specific indicator in a specific year
	 */
	public Observation getObservation(String year, String indicator,
			String countryCode) {
		checkValidYear(year);
		countryCode = checkValidCountryCode(countryCode);
		checkValidIndicator(indicator);
		return data.getObservation(year, indicator, countryCode);
	}

	/**
	 * Gets the {@link Observation}s of a {@link Country} in a specific year
	 * 
	 * @param year
	 *            The year to be checked
	 * @param countryCode
	 *            The iso-alpha-2 or iso-alpha-3 code of the {@link Country} to
	 *            be checked
	 * @return The {@link Observation}s of a {@link Country} in a specific year
	 */
	public ObservationCollection getObservation(String year, String countryCode) {
		checkValidYear(year);
		countryCode = checkValidCountryCode(countryCode);
		ObservationCollection obs = new ObservationCollection();
		obs.setObservations(data.getObservation(year, countryCode));
		return obs;
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
	 *            The iso-alpha-2 or iso-alpha-3 code of the {@link Country} to
	 *            be checked
	 * @return The {@link Observation}s of several {@link Country countries}
	 *         produced by a some indicators in some specific years
	 */
	public Collection<Observation> getObservations(Collection<String> years,
			Collection<String> indicators, Collection<String> countryCodes) {
		for (String year : years) {
			checkValidYear(year);
		}
		Collection<String> isoAlpha2Codes = new ArrayDeque<String>(
				countryCodes.size());
		for (String countryCode : countryCodes) {
			isoAlpha2Codes.add(checkValidCountryCode(countryCode));
		}
		for (String indicator : indicators) {
			checkValidIndicator(indicator);
		}
		return data.getObservations(years, indicators, isoAlpha2Codes);
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
		countryCode = checkValidCountryCode(countryCode);
		Country country = data.getCountryData(countryCode);
		country.setObservations(getObservation(year, countryCode)
				.getObservations());
		country.setRanks(getRank(year, countryCode).getData());
		country.setCode_iso_alpha2(countryCode);
		country.setYear(Integer.parseInt(year));
		return country;
	}

	/**
	 * Checks if a string is a valid year
	 * 
	 * @param year
	 *            The year to be checked
	 */
	private void checkValidYear(String year) {
		if (!year.matches("\\d{4}")) {
			throw new IllegalArgumentException(year + " is not a valid year");
		}
	}

	/**
	 * Checks if a string is a valid country code and returns it as iso-alpha-2
	 * code
	 * 
	 * @param countryCode
	 *            The string to be checked
	 * @return The iso-alpha-2 code
	 */
	private String checkValidCountryCode(String countryCode) {
		countryCode = countryCode.toUpperCase();
		if (countryCode.matches("[A-Z]{2}")) {
			return countryCode;
		} else {
			return convertToIso2(countryCode);
		}
	}

	/**
	 * Converts a iso-alpha-3 code into a iso-alpha-2 code
	 * 
	 * @param countryCode
	 *            The iso-alpha-3 code to be converted
	 * @return The converted iso-alpha-2 code
	 */
	private String convertToIso2(String countryCode) {
		if (countryCode.matches("[A-Z]{3}")) {
			return data.convertCountryCode(countryCode);
		} else {
			throw new IllegalArgumentException(countryCode
					+ " is not a valid countryCode");
		}
	}

	/**
	 * Checks if a string is a valid indicator
	 * 
	 * @param indicator
	 *            The indicator to be checked
	 */
	private void checkValidIndicator(String indicator) {
		if (!data.getAvailableIndicators().contains(indicator)) {
			throw new IllegalArgumentException(indicator
					+ " is not a valid indicator");
		}
	}

}
