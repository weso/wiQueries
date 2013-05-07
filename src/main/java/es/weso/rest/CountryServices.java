package es.weso.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.weso.business.CountryManagement;
import es.weso.model.Country;
import es.weso.model.ObservationCollection;

/**
 * Class to map services that get data of {@link Country countries}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 27/03/2013
 */
@Controller
public class CountryServices {

	private CountryManagement countryManagement;

	public CountryServices() {
		countryManagement = new CountryManagement();
	}

	@RequestMapping(value = "/rank/{year}/{countryCode}/{category}", method = RequestMethod.GET)
	public String rank(@PathVariable String year,
			@PathVariable String countryCode, @PathVariable String category, ModelMap map) {
		map.addAttribute(countryManagement.getRank(category, year, countryCode));
		return "rank";
	}

	@RequestMapping(value = "/rank/{year}/{countryCode}", method = RequestMethod.GET)
	public String rank(@PathVariable String year,
			@PathVariable String countryCode, ModelMap model) {
		model.addAttribute(countryManagement.getRank(year, countryCode));
		return "ranks";
	}

	@RequestMapping(value = "/observation/{year}/{countryCode}/{indicator}", method = RequestMethod.GET)
	public String observation(@PathVariable String year,
			@PathVariable String countryCode, @PathVariable String indicator,
			ModelMap model) {
		model.addAttribute(countryManagement.getObservation(year, indicator,
				countryCode));
		return "observation";
	}

	@RequestMapping(value = "/observation/{year}/{countryCode}", method = RequestMethod.GET)
	public String observation(@PathVariable String year,
			@PathVariable String countryCode, ModelMap model) {
		ObservationCollection oc = new ObservationCollection();
		oc.setObservations(countryManagement.getObservation(year, countryCode));
		model.addAttribute(oc);
		return "observations";
	}

	@RequestMapping(value = "/country/{year}/{countryCode}", method = RequestMethod.GET)
	public String country(@PathVariable String year,
			@PathVariable String countryCode, ModelMap model) {
		model.addAttribute(countryManagement.getCountry(year, countryCode));
		return "country";
	}
}
