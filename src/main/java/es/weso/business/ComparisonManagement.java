package es.weso.business;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.weso.model.Observation;
import es.weso.wirouter.CountryRouteParser;
import es.weso.wirouter.YearRouteParser;
import es.weso.wirouter.country.CountryExpr;
import es.weso.wirouter.year.YearExpr;

/**
 * Class that manages the comparison of {@link Observation}s
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 27/03/2013
 */
public class ComparisonManagement {

	private CountryManagement countryManagement;

	public ComparisonManagement() {
		countryManagement = new CountryManagement();
	}

	/**
	 * Compares several countries in several years with several indicators
	 * 
	 * @param years
	 *            The years to be compared in a format that can be parsed by
	 *            {@link YearRouteParser}
	 * @param countryCodes
	 *            The countries to be compared in a format that can be parsed by
	 *            {@link CountryRouteParser}
	 * @param indicators
	 *            The indicators to be compared sepparated by commas
	 * @return The {@link Observation}s to be compared
	 */
	public Collection<Observation> compareCountries(String years,
			String countryCodes, String indicators) {
		List<CountryExpr> cExpr = new CountryRouteParser()
				.parseRoute(countryCodes);
		Collection<String> yearCol = getYears(years);
		Collection<Observation> result = new ArrayDeque<Observation>(
				yearCol.size() * cExpr.size() * 3
						* indicators.split(",").length);
		for (CountryExpr expr : cExpr) {
			Collection<String> codes = expr.getCountryCodes();
			Collection<Observation> obs = countryManagement.getObservations(
					yearCol, Arrays.asList(indicators.split(",")), codes);
			result.addAll(groupObservations(expr.toString(), codes.size(), obs));
		}
		return result;
	}

	/**
	 * Groups {@link Observation}s by indicator and year
	 * 
	 * @param name
	 *            The name of the country observed
	 * @param size
	 *            The amount of countries grouped
	 * @param obs
	 *            The {@link Observation}s to group
	 * @return The {@link Observation}s grouped by indicator and year
	 */
	private Collection<Observation> groupObservations(String name, int size,
			Collection<Observation> obs) {
		Map<String, Collection<Observation>> classified = classifyObservations(
				size, obs);
		Collection<Observation> result = new ArrayDeque<Observation>(classified
				.values().size());
		for (Collection<Observation> clasobs : classified.values()) {
			Observation avg = getAvg(name, clasobs);
			if (avg != null) {
				result.add(avg);
			}
		}
		return result;
	}

	/**
	 * Parses the year parameter
	 * 
	 * @param years
	 *            The String to be parsed
	 * @return The collection of years to be observed
	 */
	private Collection<String> getYears(String years) {
		List<YearExpr> yExpr = new YearRouteParser().parseRoute(years);
		Collection<String> yearCollection = new ArrayDeque<String>(
				yExpr.size() * 2);
		for (YearExpr expr : yExpr) {
			yearCollection.addAll(expr.getYears());
		}
		return yearCollection;
	}

	/**
	 * Gets the average of a collection of {@link Observation}s
	 * 
	 * @param name
	 *            The name to be given to the group of countries observed
	 * @param obs
	 *            The {@link Observation}s to be used in the calculation
	 * @return The average of the {@link Observation}s
	 */
	private Observation getAvg(String name, Collection<Observation> obs) {
		Observation first = null;
		for (Observation ob : obs) {
			if (first == null) {
				first = ob;
				first.setCountryName(name);
				first.setCountryUri("");
				first.setUri("");
				first.setLabel(first.getIndicatorName() + " for " + name
						+ " during " + first.getYear());
			} else {
				first.setValue(first.getValue() + ob.getValue());
			}
		}
		if (first != null) {
			first.setValue(first.getValue() / obs.size());
		}
		return first;
	}

	/**
	 * Classifies {@link Observation}s by year and indicator
	 * 
	 * @param size
	 *            The amount of countries in each group
	 * @param obs
	 *            The {@link Observation}s to be classified
	 * @return The {@link Observation}s classified by year and indicator
	 */
	private Map<String, Collection<Observation>> classifyObservations(int size,
			Collection<Observation> obs) {
		Map<String, Collection<Observation>> classified = new HashMap<String, Collection<Observation>>();
		for (Observation ob : obs) {
			if (!classified.containsKey(ob.getIndicatorName() + "@"
					+ ob.getYear())) {
				classified.put(ob.getIndicatorName() + "@" + ob.getYear(),
						new ArrayDeque<Observation>(size));
			}
			classified.get(ob.getIndicatorName() + "@" + ob.getYear()).add(ob);
		}
		return classified;
	}
}
