package es.weso.rest;

import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.weso.business.ComparisonManagement;
import es.weso.model.Country;
import es.weso.model.Observation;

/**
 * Class to map services that will compare {@link Country countries}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 27/03/2013
 */
@Controller
@RequestMapping("/compare")
public class CompareServices {

	private ComparisonManagement comparisonManager;

	public CompareServices() {
		comparisonManager = new ComparisonManagement();
	}

	@RequestMapping(value = "/{years}/{countryCodes}/{categories}", method = RequestMethod.GET)
	public Collection<Observation> compareCountries(@PathVariable String years,
			@PathVariable String countryCodes, @PathVariable String categories) {
		return comparisonManager.compareCountries(years, countryCodes,
				categories);
	}
}
