package es.weso.rest;

import org.springframework.stereotype.Controller;

import es.weso.business.CountryManagement;
import es.weso.model.Country;

/**
 * Class to map services that will compare {@link Country countries}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 27/03/2013
 */
@Controller(value="/compare")
public class CompareServices {

	private CountryManagement countryManagement;

	public CompareServices() {
		countryManagement = new CountryManagement();
	}
}
