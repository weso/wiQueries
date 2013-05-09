package es.weso.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.weso.business.RegionManagement;
import es.weso.model.Region;

/**
 * Class to map services that get data of {@link Region}s
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 08/05/2013
 */
@Controller
@RequestMapping(value = "/region/")
public class RegionServices {

	private RegionManagement regionManagement;

	public RegionServices() {
		this.regionManagement = new RegionManagement();
	}

	@RequestMapping(value = "{year}/{regionName}", method = RequestMethod.GET)
	public String regionName(@PathVariable String year,
			@PathVariable String regionName, ModelMap model) {
		model.addAttribute(regionManagement.getRegion(regionName, year));
		return "region";
	}
}
