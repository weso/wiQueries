package es.weso.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.weso.business.PeopleManagement;
import es.weso.model.Person;

/**
 * Class to map services that get data of {@link Person people}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 09/05/2013
 */
@Controller
@RequestMapping(value = "/people/")
public class PersonServices {

	private PeopleManagement peopleManagement;
	
	public PersonServices() {
		peopleManagement = new PeopleManagement();
	}
	
	@RequestMapping(value = "{nick}", method = RequestMethod.GET)
	public String rank(@PathVariable String nick, ModelMap map) {
		map.addAttribute(peopleManagement.getPerson(nick));
		return "person";
	}
	
	
}
