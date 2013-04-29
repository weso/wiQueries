package es.weso.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.weso.business.CountryManagement;
import es.weso.model.CountryCollection;

/**
 * Class to map services that perform auxiliar operations
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 25/04/2013
 */
@Controller
public class AuxiliarServices {

	private CountryManagement countryManagement;

	public AuxiliarServices() {
		countryManagement = new CountryManagement();
	}

	@RequestMapping(value = "/{region}/countries", method = RequestMethod.GET)
	public CountryCollection getCountriesFromRegion(@PathVariable String region) {
		CountryCollection cc = new CountryCollection();
		cc.setCountries(countryManagement.getRegionCountries(region));
		return cc;
	}
}
