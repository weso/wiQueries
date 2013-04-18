package es.weso.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.weso.business.CountryManagement;
import es.weso.model.Country;
import es.weso.model.Observation;
import es.weso.model.ObservationCollection;
import es.weso.model.Rank;
import es.weso.model.RankMap;

/**
 * Class to map services that get data of {@link Country countries}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 27/03/2013
 */
@Controller(value="/single")
public class CountryServices {

	private CountryManagement countryManagement;

	public CountryServices() {
		countryManagement = new CountryManagement();
	}

	@RequestMapping(value = "/rank/{year}/{countryCode}/{category}", method = RequestMethod.GET)
	public Rank rank(@PathVariable String year,
			@PathVariable String countryCode, @PathVariable String category) {
		return countryManagement.getRank(category, year, countryCode);
	}

	@RequestMapping(value = "/rank/{year}/{countryCode}", method = RequestMethod.GET)
	public RankMap rank(@PathVariable String year,
			@PathVariable String countryCode) {
		return countryManagement.getRank(year, countryCode);
	}

	@RequestMapping(value = "/observation/{year}/{countryCode}/{indicator}", method = RequestMethod.GET)
	public Observation observation(@PathVariable String year,
			@PathVariable String countryCode, @PathVariable String indicator) {
		return countryManagement.getObservation(year, indicator, countryCode);
	}

	@RequestMapping(value = "/observation/{year}/{countryCode}", method = RequestMethod.GET)
	public ObservationCollection observation(@PathVariable String year,
			@PathVariable String countryCode) {
		return countryManagement.getObservation(year, countryCode);
	}

	@RequestMapping(value = "/country/{year}/{countryCode}", method = RequestMethod.GET)
	public Country country(@PathVariable String year,
			@PathVariable String countryCode) {
		return countryManagement.getCountry(year, countryCode);
	}
}
