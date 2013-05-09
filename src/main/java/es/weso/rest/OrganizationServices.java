package es.weso.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.weso.business.OrganizationManagement;
import es.weso.model.Organization;

/**
 * Class to map services that get data of {@link Organization}s
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 09/05/2013
 */
@Controller
@RequestMapping(value = "/organization/")
public class OrganizationServices {

	private OrganizationManagement organizationManagement;

	public OrganizationServices() {
		organizationManagement = new OrganizationManagement();
	}

	@RequestMapping(value = "{identifier}", method = RequestMethod.GET)
	public String rank(@PathVariable String identifier, ModelMap map) {
		map.addAttribute(organizationManagement.getOrganization(identifier));
		return "organization";
	}

}
