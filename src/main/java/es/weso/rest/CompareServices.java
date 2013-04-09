package es.weso.rest;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.weso.data.CountryData;
import es.weso.model.Country;
import es.weso.model.Observation;
import es.weso.model.Rank;

/**
 * Class to map services that will compare {@link Country countries}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 27/03/2013
 */
@Controller
public class CompareServices {

	private CountryData data;

	public CompareServices() {
		data = new CountryData();
	}

	@RequestMapping(value = "/rank/{year}/{countryCode}/{category}", method = RequestMethod.GET)
	@ResponseBody
	public Rank rank(@PathVariable String year,
			@PathVariable String countryCode, @PathVariable String category) {
		return data.getRank(category, year, countryCode);
	}

	@RequestMapping(value = "/rank/{year}/{countryCode}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Rank> rank(@PathVariable String year,
			@PathVariable String countryCode) {
		return data.getRank(year, countryCode);
	}

	@RequestMapping(value = "/observation/{year}/{countryCode}/{indicator}", method = RequestMethod.GET)
	@ResponseBody
	public Observation observation(@PathVariable String year,
			@PathVariable String countryCode, @PathVariable String indicator) {
		return data.getObservation(year, indicator, countryCode.toUpperCase());
	}

	@RequestMapping(value = "/observation/{year}/{countryCode}", method = RequestMethod.GET)
	@ResponseBody
	public Collection<Observation> observation(@PathVariable String year,
			@PathVariable String countryCode) {
		return data.getObservation(year, countryCode.toUpperCase());
	}

	@RequestMapping(value = "/country/{year}/{countryCode}", method = RequestMethod.GET)
	@ResponseBody
	public Country country(@PathVariable String year,
			@PathVariable String countryCode) {
		return data.getCountry(year, countryCode.toUpperCase());
	}
}
