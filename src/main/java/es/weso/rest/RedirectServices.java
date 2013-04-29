package es.weso.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RedirectServices {

	@RequestMapping(value = "/version2012/{type}/{value}", method = RequestMethod.GET)
	public void redirection(@PathVariable String type,
			@PathVariable String value) {

	}

	@RequestMapping(value = "/version2012/{type}/{subtype}/{value}", method = RequestMethod.GET)
	public void redirectionWithSubtype(@PathVariable String type,
			@PathVariable String subType, @PathVariable String value) {

	}
}
